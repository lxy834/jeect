package org.jeecg.ftu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.ftu.entity.FtuDevice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.ftu.entity.FtuElectlVolume;
import org.jeecg.ftu.vo.FtuElectlVolumeVO;

/**
 * @Description: FTU终端
 * @Author: jeecg-boot
 * @Date:   2025-06-23
 * @Version: V1.0
 */
public interface FtuDeviceMapper extends BaseMapper<FtuDevice> {
    List<FtuElectlVolumeVO> queryFtuElectlVolumeList();
}
