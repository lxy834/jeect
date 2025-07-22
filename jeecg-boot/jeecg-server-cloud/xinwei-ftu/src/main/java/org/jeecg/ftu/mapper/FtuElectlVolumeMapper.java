package org.jeecg.ftu.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.ftu.entity.FtuElectlVolume;

import java.util.List;

/**
 * @Description: 电气量信息
 * @Author: jeecg-boot
 * @Date: 2025-06-23
 * @Version: V1.0
 */
public interface FtuElectlVolumeMapper extends BaseMapper<FtuElectlVolume> {

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
     * @return List<FtuElectlVolume>
     */
    public List<FtuElectlVolume> selectByMainId(@Param("mainId") String mainId);

    @InterceptorIgnore(tenantLine = "true")
    void insertVolume(@Param("id") String id, @Param("v") double v, @Param("current") double current, @Param("ftuId") String ftuId, @Param("sendMode") Integer sendMode);
}
