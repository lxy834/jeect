package org.jeecg.ftu.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.ftu.entity.FtuWarnInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.ftu.vo.WarnInfoStatVO;

/**
 * @Description: 告警记录
 * @Author: jeecg-boot
 * @Date:   2025-06-30
 * @Version: V1.0
 */
public interface FtuWarnInfoMapper extends BaseMapper<FtuWarnInfo> {
    /**
     * 通过主表id删除子表数据
     *
     * @param mainId 主表id
     * @return boolean
     */
    boolean deleteByMainId(@Param("mainId") String mainId);

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<FtuWarnInfo>
     */
    List<FtuWarnInfo> selectByMainId(@Param("mainId") String mainId);

    List<Map<String, Object>> getDailyAlarmStatisticsGroupedByType(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("ftuId") String ftuId
    );

    List<WarnInfoStatVO> eventStat();


}
