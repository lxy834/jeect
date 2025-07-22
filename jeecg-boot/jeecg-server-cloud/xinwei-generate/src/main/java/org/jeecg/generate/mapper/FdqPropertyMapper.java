package org.jeecg.generate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.generate.entity.FdqProperty;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description: 应急装备
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
public interface FdqPropertyMapper extends BaseMapper<FdqProperty> {
    Map<String, BigDecimal> selectCountKwh(@Param("plate") String plate);
}
