package org.jeecg.generate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.generate.dto.FdqTrackDTO;
import org.jeecg.generate.entity.FdqTrack;
import org.jeecg.generate.mapper.FdqTrackMapper;
import org.jeecg.generate.service.IFdqTrackService;
import org.jeecg.generate.vo.FdqTrackVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 定位数据
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Service
public class FdqTrackServiceImpl extends ServiceImpl<FdqTrackMapper, FdqTrack> implements IFdqTrackService {

    @Override
    public List<FdqTrackVO> getTrackByPlate(FdqTrackDTO trackDTO) {
        return baseMapper.getByAssetNumber(trackDTO);
    }
}
