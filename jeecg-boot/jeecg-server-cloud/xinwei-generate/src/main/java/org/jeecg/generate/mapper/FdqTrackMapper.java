package org.jeecg.generate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.generate.dto.FdqTrackDTO;
import org.jeecg.generate.entity.FdqTrack;
import org.jeecg.generate.vo.FdqTrackVO;

import java.util.List;

/**
 * @Description: 定位数据
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
public interface FdqTrackMapper extends BaseMapper<FdqTrack> {

    List<FdqTrackVO> getByAssetNumber(@Param("dto") FdqTrackDTO dto);

}
