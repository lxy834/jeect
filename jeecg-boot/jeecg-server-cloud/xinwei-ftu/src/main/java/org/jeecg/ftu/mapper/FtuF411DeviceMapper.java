package org.jeecg.ftu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.ftu.entity.FtuF411Device;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 通信终端
 * @Author: jeecg-boot
 * @Date:   2025-06-23
 * @Version: V1.0
 */
public interface FtuF411DeviceMapper extends BaseMapper<FtuF411Device> {

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
     * @return List<FtuF411Device>
     */
    public List<FtuF411Device> selectByMainId(@Param("mainId") String mainId);
}
