package org.jeecg.ftu.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.ftu.entity.FtuDevice;
import org.jeecg.ftu.vo.BindVO;
import org.jeecg.ftu.vo.FtuDeviceMapVO;
import org.jeecg.ftu.vo.FtuElectlVolumeVO;

import java.util.List;

/**
 * @Description: FTU终端
 * @Author: jeecg-boot
 * @Date: 2025-06-23
 * @Version: V1.0
 */
public interface FtuDeviceMapper extends BaseMapper<FtuDevice> {

    List<FtuElectlVolumeVO> queryFtuElectlVolumeList();

    @InterceptorIgnore(tenantLine = "true")
    FtuDevice dataById(@Param("id") String id);

    @InterceptorIgnore(tenantLine = "true")
    boolean updateLngLat(@Param("lng") double lng, @Param("lat") double lat, @Param("status") Integer status, @Param("id") String id);

    List<FtuDeviceMapVO> getIndexList();

    List<BindVO> ids();

}
