package org.jeecg.generate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.jeecg.generate.entity.FdqController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 历史数据
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
public interface FdqControllerMapper extends BaseMapper<FdqController> {

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
     * @return List<FdqController>
     */
    public List<FdqController> selectByMainId(@Param("mainId") String mainId);

    Map<String, BigDecimal> selectCountKwh();

    // Mapper 接口
    @MapKey("ORDER_TYPE")
    Map<String, Map<String, Long>> selectOrderCount();


}
