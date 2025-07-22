package org.jeecg.generate.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jeecg.common.util.Gps;
import org.jeecg.common.util.GpsTransfer;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.generate.entity.*;
import org.jeecg.generate.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ConsumerListener {

    public static final String ORDER_CACHE_PREFIX = "order_";
    public static final String TIMEOUT_ORDER_CACHE_PREFIX = "timeout_order_";
    public static final long TIMEOUT_ORDER_EXPIRE = 20 * 60L; // 20分钟，单位：秒
    private static final Logger log = LoggerFactory.getLogger(ConsumerListener.class);
    @Autowired
    private IFdqPropertyService fdqPropertyService;
    @Autowired
    private IFdqControllerService fdqControllerService;
    @Autowired
    private IFdqTrackService trackService;
    @Autowired
    private IFdqHeartBeatService heartBeatService;
    @Autowired
    private IFdqOrderService orderService;
    @Autowired
    private IFdqOrderStepService stepService;
    @Autowired
    private RedisUtil redisUtil;

    @KafkaListener(topics = {"${spring.kafka.consumer.topic-name}"})
    public void message(final ConsumerRecord<String, String> record) {
        String msg = record.value();
        msg = msg.replaceAll(" ", "");
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            String messageType = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).get("serviceId").toString();
            String card = (String) jsonObject.getJSONArray("devices").getJSONObject(0).get("deviceId");
            switch (messageType) {
                case "Controller" -> saveController(jsonObject, card);
                case "Location" -> saveLocation(jsonObject, card);
                case "HeartBeat" -> saveHeartBeat(jsonObject, card);
                default -> log.warn("错误数据：{}", msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveHeartBeat(JSONObject jsonObject, String card) {
        FdqProperty property = fdqPropertyService.getOne(Wrappers.<FdqProperty>query().lambda().eq(FdqProperty::getSn, card));
        if (property != null) {
            JSONObject dataJson = jsonObject.getJSONArray("devices")
                    .getJSONObject(0)
                    .getJSONArray("services")
                    .getJSONObject(0)
                    .getJSONObject("data");
            System.out.println(dataJson);
            FdqHeartBeat heartBeat = dataJson.toJavaObject(FdqHeartBeat.class);
            heartBeat.setPlateNumber(property.getPlateNumber());
            heartBeatService.save(heartBeat);
        }
    }

    public void saveController(JSONObject json, String card) {
        // 1. 获取设备属性并校验
        FdqProperty property = fdqPropertyService.getOne(
                Wrappers.<FdqProperty>query().lambda().eq(FdqProperty::getSn, card)
        );
        if (property == null) {
            throw new IllegalArgumentException("未找到SN为" + card + "的设备属性信息");
        }

        // 2. 安全提取JSON数据（避免空指针异常）
        JSONObject dataJson = extractDataJson(json);
        if (dataJson == null) {
            throw new IllegalArgumentException("JSON数据格式不正确，无法提取data节点");
        }

        // 3. 转换为实体对象
        FdqController controller = dataJson.toJavaObject(FdqController.class);
        controller.setPlateNumber(property.getPlateNumber());
        if (controller.getWaterTemperature() == 32768) {
            controller.setWaterTemperature(0.0);
        }

        // 4. 处理发电状态相关的工单逻辑
        if (isGeneratingElectricity(controller)) {
            String plateNumber = controller.getPlateNumber();
            property.setLastStatus(0);
            fdqPropertyService.updateById(property);
            if (redisUtil.hasKey(ORDER_CACHE_PREFIX + plateNumber)) {
                handleExistingOrder(plateNumber, controller);
            } else if (redisUtil.hasKey(TIMEOUT_ORDER_CACHE_PREFIX + plateNumber)) {
                refreshTimeoutOrderCache(plateNumber);
            } else {
                createNewOrder(controller, property);
            }
        }

        // 5. 保存控制器数据
        fdqControllerService.save(controller);
    }

    public void saveLocation(JSONObject json, String card) {
        FdqProperty property = fdqPropertyService.getOne(Wrappers.<FdqProperty>query().lambda().eq(FdqProperty::getSn, card));
        if (property != null) {
            JSONArray jsonArray = json.getJSONArray("devices")
                    .getJSONObject(0)
                    .getJSONArray("services")
                    .getJSONObject(0)
                    .getJSONArray("data");
            List<FdqTrack> trackList = new ArrayList<>();
            jsonArray.forEach(arr -> {
                JSONObject dataJson = (JSONObject) arr;
                FdqTrack track = dataJson.toJavaObject(FdqTrack.class);
                Gps gps = GpsTransfer.wgs84_To_Gcj02(track.getLat(), track.getLng());
                track.setPlateNumber(property.getPlateNumber());
                track.setLat(gps.getLat());
                track.setLng(gps.getLon());
                trackList.add(track);

            });
            trackService.saveBatch(trackList);
            property.setLastStatus(Integer.valueOf(trackList.get(trackList.size() - 1).getMotionStatus()));
            property.setLastLng(trackList.get(trackList.size() - 1).getLng());
            property.setLastLat(trackList.get(trackList.size() - 1).getLat());
            fdqPropertyService.updateById(property);
        }
    }

    /**
     * 从JSON中安全提取data节点
     */
    private JSONObject extractDataJson(JSONObject json) {
        try {
            return json.getJSONArray("devices")
                    .getJSONObject(0)
                    .getJSONArray("services")
                    .getJSONObject(0)
                    .getJSONObject("data");
        } catch (Exception e) {
            // 日志记录：JSON解析异常
            log.error("提取data节点失败", e);
            return null;
        }
    }

    /**
     * 判断是否处于发电状态
     */
    private boolean isGeneratingElectricity(FdqController controller) {
        return controller.getLineVoltageA() > 0 && controller.getCurrentA() > 0;
    }

    private void handleExistingOrder(String plateNumber, FdqController controller) {
        // 获取工单并转换（避免重复JSON操作）
        FdqOrder order = getOrderFromCache(ORDER_CACHE_PREFIX + plateNumber);
        if (order == null) {
            return;
        }

        // 更新工单状态
        order.setOrderStatus("正在发电");
        order.setBeginTime(new Date());
        orderService.updateById(order);

        // 更新工单步骤
        updateOrderSteps(order.getId());

        // 维护缓存
        redisUtil.del(ORDER_CACHE_PREFIX + plateNumber);
        redisUtil.set(
                TIMEOUT_ORDER_CACHE_PREFIX + plateNumber,
                JSON.toJSONString(order),
                TIMEOUT_ORDER_EXPIRE
        );
    }

    /**
     * 刷新超时工单缓存时间
     */
    private void refreshTimeoutOrderCache(String plateNumber) {
        FdqOrder order = getOrderFromCache(TIMEOUT_ORDER_CACHE_PREFIX + plateNumber);
        if (order != null) {
            redisUtil.set(
                    TIMEOUT_ORDER_CACHE_PREFIX + plateNumber,
                    JSON.toJSONString(order),
                    TIMEOUT_ORDER_EXPIRE
            );
        }
    }

    /**
     * 创建新工单
     */
    private void createNewOrder(FdqController controller, FdqProperty property) {
        // 创建主工单
        FdqOrder order = new FdqOrder();
        order.setPlateNumber(controller.getPlateNumber());
        order.setBureau(property.getBureau());
        order.setDeptName(property.getDeptName());
        order.setOrderType("应急发电任务");
        order.setTargetUser("未知");
        order.setOrderStatus("正在发电");
        order.setId(IdWorker.getIdStr());
        orderService.save(order);

        // 创建订工单步骤（使用预定义配置简化逻辑）
        List<FdqOrderStep> stepList = createOrderSteps(order.getId());
        stepService.saveBatch(stepList);
    }

    /**
     * 从缓存获取工单对象
     */
    private FdqOrder getOrderFromCache(String cacheKey) {
        try {
            String orderJson = redisUtil.get(cacheKey).toString();
            return JSON.parseObject(orderJson, FdqOrder.class);
        } catch (Exception e) {
            log.error("从缓存获取工单失败，key:{}", cacheKey, e);
            return null;
        }
    }

    /**
     * 更新工单步骤状态
     */
    private void updateOrderSteps(String orderId) {
        List<FdqOrderStep> stepList = stepService.list(
                Wrappers.<FdqOrderStep>query().lambda().eq(FdqOrderStep::getOrderId, orderId)
        );

        for (FdqOrderStep step : stepList) {
            if (step.getStep() == 1) {
                step.setStatus("success");
            } else if (step.getStep() == 2) {
                step.setStatus("process");
                step.setStepTime(new Date());
                step.setStepInfo("正在发电");
            }
        }

        stepService.updateBatchById(stepList);
    }

    /**
     * 创建工单步骤（使用配置化方式，减少重复代码）
     */
    private List<FdqOrderStep> createOrderSteps(String orderId) {
        // 预定义步骤配置
        StepConfig[] stepConfigs = {
                new StepConfig(0, "success", new Date(), "收到工单，准备前往现场"),
                new StepConfig(1, "success", new Date(), "前往现场"),
                new StepConfig(2, "process", new Date(), "正在发电"),
                new StepConfig(3, "wait", null, "归还设备")
        };

        List<FdqOrderStep> stepList = new ArrayList<>(stepConfigs.length);
        for (StepConfig config : stepConfigs) {
            FdqOrderStep step = new FdqOrderStep();
            step.setOrderId(orderId);
            step.setStep(config.step);
            step.setStatus(config.status);
            step.setStepTime(config.time);
            step.setStepInfo(config.info);
            stepList.add(step);
        }

        return stepList;
    }

    // 内部辅助类：封装步骤配置
    private static class StepConfig {
        int step;
        String status;
        Date time;
        String info;

        StepConfig(int step, String status, Date time, String info) {
            this.step = step;
            this.status = status;
            this.time = time;
            this.info = info;
        }
    }
}
