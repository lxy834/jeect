package org.jeecg.ftu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.ftu.entity.FtuWarnInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
    public boolean deleteByMainId(@Param("mainId") String mainId);

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<FtuWarnInfo>
     */
    public List<FtuWarnInfo> selectByMainId(@Param("mainId") String mainId);
}
