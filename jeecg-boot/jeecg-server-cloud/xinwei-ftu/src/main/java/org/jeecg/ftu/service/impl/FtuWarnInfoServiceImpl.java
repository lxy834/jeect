package org.jeecg.ftu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.ftu.entity.FtuWarnInfo;
import org.jeecg.ftu.mapper.FtuWarnInfoMapper;
import org.jeecg.ftu.service.IFtuWarnInfoService;
import org.jeecg.ftu.vo.WarnInfoStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Description: 告警记录
 * @Author: jeecg-boot
 * @Date: 2025-06-30
 * @Version: V1.0
 */
@Service
public class FtuWarnInfoServiceImpl extends ServiceImpl<FtuWarnInfoMapper, FtuWarnInfo> implements IFtuWarnInfoService {

    @Autowired
    private FtuWarnInfoMapper ftuWarnInfoMapper;

    @Override
    public List<FtuWarnInfo> selectByMainId(String mainId) {
        return ftuWarnInfoMapper.selectByMainId(mainId);
    }

    public Map<String, Object> getStatById(String ftuId) {
        // 参数检查
        if (ftuId == null || ftuId.trim().isEmpty()) {
            return createEmptyChartData();
        }
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate endDate = LocalDate.now(zoneId);
        LocalDate startDate = endDate.minusDays(6);

        String startDateStr = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String endDateStr = endDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        // 查询数据库
        List<Map<String, Object>> sqlResults =
                baseMapper.getDailyAlarmStatisticsGroupedByType(startDateStr, endDateStr, ftuId);
        // 转换为图表数据
        return convertToChartData(sqlResults, startDate, endDate);
    }

    @Override
    public IPage<FtuWarnInfo> getEventById(String ftuId, String date, String event, Integer pageNo, Integer pageSize) {
        Page<FtuWarnInfo> page = new Page<>(pageNo, pageSize);
        return baseMapper.selectPage(page, Wrappers.<FtuWarnInfo>query().lambda()
                .eq(FtuWarnInfo::getDeviceType, event).eq(FtuWarnInfo::getDeviceId, ftuId).likeRight(FtuWarnInfo::getCreateTime, date));
    }

    @Override
    public Map<String, Object> eventStat() {
        // 预先定义好所有可能的告警类型及其对应键名
        Map<String, String> warnTypeMapping = Map.of(
                "故障", "fault",
                "其它", "otherEvent",
                "掉线", "disconnect",
                "掉电", "powerDown"
        );

        // 查询所有告警类型的统计数据
        List<WarnInfoStatVO> warnInfoStatVOS = ftuWarnInfoMapper.eventStat();

        // 提前计算Map初始容量，避免后续扩容开销
        Map<String, Object> resultMap = new HashMap<>(warnInfoStatVOS.size());

        // 初始化所有可能的告警类型计数为0
        for (String key : warnTypeMapping.values()) {
            resultMap.put(key, 0);
        }

        // 处理查询结果，直接通过映射关系设置值
        for (WarnInfoStatVO vo : warnInfoStatVOS) {
            String targetKey = warnTypeMapping.get(vo.getWarnType());
            if (targetKey != null) {
                resultMap.put(targetKey, vo.getWarnCount());
            }
        }

        return resultMap;
    }

    private Map<String, Object> convertToChartData(List<Map<String, Object>> sqlResults, LocalDate startDate, LocalDate endDate) {

        // 预分配足够大小的Map以减少扩容开销
        Map<String, Map<String, Integer>> typeGroupedStats = new LinkedHashMap<>((int) ((int) (endDate.toEpochDay() - startDate.toEpochDay() + 1) * 1.5));

        // 直接处理SQL结果，避免Stream开销
        for (Map<String, Object> row : sqlResults) {
            String date = (String) row.get("create_time");

            // 先获取值并处理null情况
            Object switchActionObj = row.get("switch_action");
            int switchAction = switchActionObj != null ? ((Number) switchActionObj).intValue() : 0;

            Object faultAlarmObj = row.get("fault_alarm");
            int faultAlarm = faultAlarmObj != null ? ((Number) faultAlarmObj).intValue() : 0;

            Object commExceptionObj = row.get("communication_exception");
            int commException = commExceptionObj != null ? ((Number) commExceptionObj).intValue() : 0;

            typeGroupedStats.put(date, Map.of(
                    "switch_action", switchAction,
                    "fault_alarm", faultAlarm,
                    "communication_exception", commException
            ));
        }

        Map<String, Object> result = new HashMap<>();
        List<String> xAxisData = new ArrayList<>();
        Map<String, List<Integer>> seriesData = new LinkedHashMap<>();

        // 初始化系列数据
        seriesData.put("switch", new ArrayList<>());
        seriesData.put("warn", new ArrayList<>());
        seriesData.put("comm", new ArrayList<>());

        // 日期格式化器
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MM-dd");

        // 遍历日期范围
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            String displayDateStr = date.format(displayFormatter);
            xAxisData.add(displayDateStr);

            // 获取该日期的统计数据
            Map<String, Integer> dailyStats = typeGroupedStats.getOrDefault(dateStr, Collections.emptyMap());

            // 添加各类型数据
            seriesData.get("switch").add(dailyStats.getOrDefault("switch_action", 0));
            seriesData.get("warn").add(dailyStats.getOrDefault("fault_alarm", 0));
            seriesData.get("comm").add(dailyStats.getOrDefault("communication_exception", 0));
        }

        result.put("xAxisData", xAxisData);
        result.put("seriesData", seriesData);
        return result;
    }

    private Map<String, Object> createEmptyChartData() {
        // 创建空图表数据，用于参数无效或无数据的情况
        Map<String, Object> result = new HashMap<>();
        List<String> xAxisData = new ArrayList<>();
        Map<String, List<Integer>> seriesData = new LinkedHashMap<>();

        // 初始化系列数据
        seriesData.put("开关动作", new ArrayList<>());
        seriesData.put("故障报警", new ArrayList<>());
        seriesData.put("通信异常", new ArrayList<>());

        // 添加7天的空数据
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MM/dd");

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            xAxisData.add(date.format(displayFormatter));
            seriesData.get("开关动作").add(0);
            seriesData.get("故障报警").add(0);
            seriesData.get("通信异常").add(0);
        }

        result.put("xAxisData", xAxisData);
        result.put("seriesData", seriesData);
        return result;
    }
}
