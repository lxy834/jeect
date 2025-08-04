package org.jeecg.ftu.listnenr;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jeecg.ftu.entity.FtuDevice;
import org.jeecg.ftu.entity.FtuF411Device;
import org.jeecg.ftu.entity.FtuWarnInfo;
import org.jeecg.ftu.service.IFtuDeviceService;
import org.jeecg.ftu.service.IFtuElectlVolumeService;
import org.jeecg.ftu.service.IFtuF411DeviceService;
import org.jeecg.ftu.service.IFtuWarnInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author averice
 */
@Component
public class ConsumerListener {

    private static final Logger log = LoggerFactory.getLogger(ConsumerListener.class);
    @Autowired
    private IFtuDeviceService deviceService;
    @Autowired
    private IFtuF411DeviceService ftuF411DeviceService;
    @Autowired
    private IFtuElectlVolumeService volumeService;
    @Autowired
    private IFtuWarnInfoService warnInfoService;

    @KafkaListener(topics = {"${spring.kafka.consumer.topic-name}"})
    public void message(final ConsumerRecord<String, String> record) {
        String msg = record.value();
        msg = msg.replaceAll(" ", "");
        log.info(msg);
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            // 解析消息类型（仍从第一个设备的serviceId获取，兼容单设备场景）
            String messageType = jsonObject.getJSONArray("devices").getJSONObject(0)
                    .getJSONArray("services").getJSONObject(0).getString("serviceId");

            switch (messageType) {
                case "Location" -> {
                    String card = jsonObject.getJSONArray("devices").getJSONObject(0).getString("deviceId");
                    saveLocation(jsonObject, card);
                }
                case "AI", "DI" -> {
                    // 遍历所有devices，处理每个设备的running数据
                    JSONArray devicesArray = jsonObject.getJSONArray("devices");
                    for (int i = 0; i < devicesArray.size(); i++) {
                        JSONObject deviceObj = devicesArray.getJSONObject(i);
                        String deviceId = deviceObj.getString("deviceId");
                        saveRunning(deviceObj, deviceId, messageType);
                    }
                }
                case "Communication" -> {
                    String card = jsonObject.getJSONArray("devices").getJSONObject(0).getString("deviceId");
                    saveCommunication(jsonObject, card);
                }
                case "CommMode" -> {
                    String card = jsonObject.getJSONArray("devices").getJSONObject(0).getString("deviceId");
                    saveCommMode(jsonObject, card);
                }
                default -> log.warn("未处理的消息类型：{}，数据：{}", messageType, msg);
            }

        } catch (Exception e) {
            log.error("处理消息异常：{}", msg, e);
        }
    }

    public void saveInfo(String warnInfo, String deviceType, String deviceId, String deviceName, String insLocation, String lineLocation, String warnType, String tenantId) {
        FtuWarnInfo info = new FtuWarnInfo();
        info.setWarnInfo(warnInfo);
        info.setDeviceType(warnType);
        info.setDeviceId(deviceId);
        info.setDeviceName(deviceName);
        info.setInsLocation(insLocation);
        info.setLineLocation(lineLocation);
        info.setWarnType(warnType);
        info.setTenantId(tenantId);
        warnInfoService.save(info);
    }

    public void saveLocation(JSONObject jsonObject, String card) {
        double lng = jsonObject.getJSONArray("devices").getJSONObject(0)
                .getJSONArray("services").getJSONObject(0)
                .getJSONObject("data").getDoubleValue("lng");
        double lat = jsonObject.getJSONArray("devices").getJSONObject(0)
                .getJSONArray("services").getJSONObject(0)
                .getJSONObject("data").getDoubleValue("lat");
        if (Objects.nonNull(card)) {
            FtuF411Device f411Device = ftuF411DeviceService.get411Device(card);
            if (Objects.nonNull(f411Device)) {
                FtuDevice device = deviceService.dataById(f411Device.getFtuId());
                deviceService.updateLngLat(lng, lat, 0, f411Device.getFtuId());
                saveInfo(device.getDeviceName() + "获取定位", "主站指令", device.getId(), device.getDeviceName(), device.getDeviceName(), device.getInsLineName(), "其他", f411Device.getTenantId());
            } else {
                log.warn("未找到card对应的FtuF411Device：{}", card);
            }
        }
    }

    // 修改：接收单个设备对象和deviceId，处理该设备的running数据
    public void saveRunning(JSONObject deviceObj, String card, String messageType) {
        if ("AI".equals(messageType)) {
            try {
                // 从当前设备对象中解析services数据（假设每个设备的services数组只有一个元素）
                JSONArray servicesArray = deviceObj.getJSONArray("services");
                if (Objects.isNull(servicesArray) || servicesArray.isEmpty()) {
                    log.warn("running类型数据中services数组为空，deviceId：{}", card);
                    return;
                }
                JSONObject serviceObj = servicesArray.getJSONObject(0);
                JSONObject data = serviceObj.getJSONObject("data");
                if (Objects.isNull(data)) {
                    log.warn("running类型数据中data为空，deviceId：{}", card);
                    return;
                }

                // 解析数据（根据实际JSON字段调整key，示例中用原代码的key）
                double currentFor211OutputB = data.getDoubleValue("currentFor211OutputB");
                double uabFor4 = data.getDoubleValue("uabFor4");
                Integer sendMode = serviceObj.getInteger("sendMode");

                if (Objects.nonNull(card)) {
                    FtuF411Device f411Device = ftuF411DeviceService.get411Device(card);
                    if (Objects.nonNull(f411Device)) {
                        volumeService.insertVolume(uabFor4, currentFor211OutputB, f411Device.getFtuId(), sendMode);
                        FtuDevice device = deviceService.dataById(f411Device.getFtuId());
                        if (Objects.nonNull(device)) {
                            saveInfo(device.getDeviceName() + "总召唤", "主站指令", device.getId(), device.getDeviceName(), device.getDeviceName(), device.getInsLineName(), "其他", f411Device.getTenantId());
                        } else {
                            log.warn("未找到ftuId对应的FtuDevice：{}，deviceId：{}", f411Device.getFtuId(), card);
                        }
                    } else {
                        log.warn("未找到card对应的FtuF411Device：{}", card);
                    }
                }
            } catch (Exception e) {
                log.error("处理running数据异常，deviceId：{}", card, e);
            }
        }
    }

    public void saveCommunication(JSONObject jsonObject, String card) {
        Integer signalFor5G = jsonObject.getJSONArray("devices").getJSONObject(0)
                .getJSONArray("services").getJSONObject(0)
                .getJSONObject("data").getInteger("signalFor5G");
        Integer signalForBd = jsonObject.getJSONArray("devices").getJSONObject(0)
                .getJSONArray("services").getJSONObject(0)
                .getJSONObject("data").getInteger("signalForBd");
        Integer statusForFtu = jsonObject.getJSONArray("devices").getJSONObject(0)
                .getJSONArray("services").getJSONObject(0)
                .getJSONObject("data").getInteger("statusForFtu");
        if (Objects.nonNull(card)) {
            ftuF411DeviceService.updateDeviceSignal(signalFor5G, signalForBd, statusForFtu, card);
        }
    }

    public void saveCommMode(JSONObject jsonObject, String card) {
        Integer mode = jsonObject.getJSONArray("devices").getJSONObject(0)
                .getJSONArray("services").getJSONObject(0)
                .getJSONObject("data").getInteger("commMode");
        if (Objects.nonNull(card)) {
            ftuF411DeviceService.updateMode(mode, card);
        }
    }

}
