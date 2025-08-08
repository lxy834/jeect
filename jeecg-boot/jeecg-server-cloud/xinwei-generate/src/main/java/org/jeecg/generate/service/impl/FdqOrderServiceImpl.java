package org.jeecg.generate.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.exception.JeecgBootBizTipException;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.generate.dto.FdqOrderDTO;
import org.jeecg.generate.entity.FdqController;
import org.jeecg.generate.entity.FdqOrder;
import org.jeecg.generate.entity.FdqOrderStep;
import org.jeecg.generate.mapper.FdqControllerMapper;
import org.jeecg.generate.mapper.FdqOrderMapper;
import org.jeecg.generate.mapper.FdqOrderStepMapper;
import org.jeecg.generate.mapper.FdqPropertyMapper;
import org.jeecg.generate.service.IFdqOrderService;
import org.jeecg.generate.vo.AppStatVO;
import org.jeecg.generate.vo.IndexOrderVO;
import org.jeecg.generate.vo.IndexStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 工单台账
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Service
public class FdqOrderServiceImpl extends ServiceImpl<FdqOrderMapper, FdqOrder> implements IFdqOrderService {

    @Autowired
    private FdqOrderMapper fdqOrderMapper;
    @Autowired
    private FdqOrderStepMapper fdqOrderStepMapper;
    @Autowired
    private FdqControllerMapper fdqControllerMapper;
    @Autowired
    private FdqPropertyMapper propertyMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(FdqOrder fdqOrder, List<FdqOrderStep> fdqOrderStepList) {
        List<FdqOrder> dbOrder = fdqOrderMapper.selectList(Wrappers.<FdqOrder>lambdaQuery()
                .eq(FdqOrder::getOrderStatus, "已结束").eq(FdqOrder::getPlateNumber, fdqOrder.getPlateNumber()));
        if (dbOrder != null && !dbOrder.isEmpty()) {
            throw new JeecgBootBizTipException("当前资产存在未结束工单");
        }
        fdqOrder.setOrderStatus("未开始");
        fdqOrderMapper.insert(fdqOrder);
        redisUtil.set("order_" + fdqOrder.getPlateNumber(), JSON.toJSONString(fdqOrder));
        if (fdqOrder.getOrderType().equals("应急发电任务")) {
            for (int i = 0; i < 4; i++) {
                FdqOrderStep step = new FdqOrderStep();
                step.setOrderId(fdqOrder.getId());
                step.setStep(i);
                if (i == 0) {
                    step.setStepTime(new Date());
                    step.setStatus("success");
                    step.setStepInfo("收到工单，准备前往现场");
                } else if (i == 1) {
                    step.setStatus("process");
                    step.setStepInfo("准备前往现场");
                } else {
                    step.setStatus("wait");
                    if (i == 3) {
                        step.setStepInfo("归还设备");
                    } else {
                        step.setStepInfo("准备开始");
                    }

                }
                fdqOrderStepMapper.insert(step);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                FdqOrderStep step = new FdqOrderStep();
                step.setOrderId(fdqOrder.getId());
                step.setStep(i);
                if (i == 0) {
                    step.setStepTime(new Date());
                    step.setStatus("process");
                    step.setStepInfo("准备开始维护");
                } else if (i == 1) {
                    step.setStatus("process");
                    step.setStepInfo("正在前往维护");
                } else {
                    step.setStatus("wait");
                    step.setStepInfo("准备开始");
                }
                fdqOrderStepMapper.insert(step);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(FdqOrder fdqOrder, List<FdqOrderStep> fdqOrderStepList) {
        fdqOrderMapper.updateById(fdqOrder);

        //1.先删除子表数据
        fdqOrderStepMapper.deleteByMainId(fdqOrder.getId());

        //2.子表数据重新插入
        if (fdqOrderStepList != null && fdqOrderStepList.size() > 0) {
            for (FdqOrderStep entity : fdqOrderStepList) {
                //外键设置
                entity.setOrderId(fdqOrder.getId());
                fdqOrderStepMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        fdqOrderStepMapper.deleteByMainId(id);
        fdqOrderMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            fdqOrderStepMapper.deleteByMainId(id.toString());
            fdqOrderMapper.deleteById(id);
        }
    }

    @Override
    public IndexOrderVO indexOrder(FdqOrderDTO fdqOrderPage) {
        IndexOrderVO vo = new IndexOrderVO();
        List<FdqOrderStep> step = fdqOrderStepMapper.selectList(Wrappers.<FdqOrderStep>query().lambda()
                .eq(FdqOrderStep::getOrderId, fdqOrderPage.getId()).orderByAsc(FdqOrderStep::getStep));
        vo.setStepList(step);
        step.forEach(v -> {
            if ("process".equals(v.getStatus())) {
                vo.setStep(v.getStep());
            }
        });
        FdqController controller = fdqControllerMapper.selectOne(Wrappers.<FdqController>query().lambda()
                .eq(FdqController::getPlateNumber, fdqOrderPage.getPlateNumber()).orderByDesc(FdqController::getCreateTime).last("limit 1"));
        vo.setController(controller);
        return vo;
    }

    @Override
    public IndexStatVO statVOResult() {
        IndexStatVO vo = new IndexStatVO();
        Map<String, BigDecimal> stringDoubleMap = fdqControllerMapper.selectCountKwh();
        int totalPower = stringDoubleMap.get("KWH").intValue();
        vo.setTotalPower(totalPower);
        vo.setTotalFuel((int) (totalPower / 3.85));
        BigDecimal fuelPerKwh = new BigDecimal("1").divide(new BigDecimal("3.85"), 2, RoundingMode.HALF_UP);
        vo.setFuelPerKwh(fuelPerKwh.doubleValue());
        vo.setCountHours(stringDoubleMap.get("RUNNING_HOURS").intValue());
        vo.setTotalRevenue((int) (totalPower * 2.5));
        Map<String, Map<String, Long>> countMap = fdqControllerMapper.selectOrderCount();
        try {
            vo.setYearlyTasks(countMap.get("应急发电任务").get("COUNT_NUM").intValue());
        } catch (Exception e) {
            vo.setYearlyTasks(0);
        }
        try {
            vo.setMaintenanceCount(countMap.get("维护任务").get("COUNT_NUM").intValue());
        } catch (Exception e) {
            vo.setMaintenanceCount(0);
        }
        return vo;
    }

    @Override
    public AppStatVO getStat() {
        // 日期处理
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate lastMonth = today.minusMonths(1);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        AppStatVO vo = new AppStatVO();
        // 处理车辆统计
        String lastMonthStr = lastMonth.format(monthFormatter);
        AppStatVO carStat = propertyMapper.getStat(lastMonthStr);
        vo.setCarCount(carStat != null ? carStat.getCarCount() : 0);
        vo.setLastCarCount(carStat != null ? carStat.getLastCarCount() : 0);

        // 处理任务统计
        String yesterdayStr = yesterday.format(dateFormatter);
        Map<String, Map<String, Long>> totalTaskMap = fdqControllerMapper.selectOrderCount();
        Map<String, Map<String, Long>> yesterdayTaskMap = fdqControllerMapper.selectOrderCountByTime(yesterdayStr);

        // 设置总任务数
        vo.setWorking(getTaskCount(totalTaskMap, "应急发电任务"));
        vo.setManual(getTaskCount(totalTaskMap, "维护任务"));

        // 设置昨日任务数
        vo.setLastWorking(getTaskCount(yesterdayTaskMap, "应急发电任务"));
        vo.setLastManual(getTaskCount(yesterdayTaskMap, "维护任务"));

        // 统计发电量和历史数据
        AppStatVO volumeStat = fdqControllerMapper.getStat();
        vo.setVolume(volumeStat != null ? volumeStat.getVolume() : 0.0);
        vo.setLastVolume(volumeStat != null ? volumeStat.getLastVolume() : 0.0);

        return vo;
    }

    /**
     * 从任务统计map中获取指定任务类型的数量
     */
    private int getTaskCount(Map<String, Map<String, Long>> taskMap, String taskType) {
        if (taskMap == null || taskMap.isEmpty()) {
            return 0;
        }

        Map<String, Long> typeMap = taskMap.get(taskType);
        if (typeMap == null || typeMap.get("COUNT_NUM") == null) {
            return 0;
        }

        return typeMap.get("COUNT_NUM").intValue();
    }


}
