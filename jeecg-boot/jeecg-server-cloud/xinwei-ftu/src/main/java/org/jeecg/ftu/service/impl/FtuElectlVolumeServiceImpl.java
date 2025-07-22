package org.jeecg.ftu.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.ftu.entity.FtuElectlVolume;
import org.jeecg.ftu.mapper.FtuElectlVolumeMapper;
import org.jeecg.ftu.service.IFtuElectlVolumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 电气量信息
 * @Author: jeecg-boot
 * @Date: 2025-06-23
 * @Version: V1.0
 */
@Service
public class FtuElectlVolumeServiceImpl extends ServiceImpl<FtuElectlVolumeMapper, FtuElectlVolume> implements IFtuElectlVolumeService {

    @Autowired
    private FtuElectlVolumeMapper ftuElectlVolumeMapper;

    @Override
    public void insertVolume(double v, double current, String ftuId, Integer sendMode) {
        ftuElectlVolumeMapper.insertVolume(IdWorker.getIdStr(), v, current, ftuId, sendMode);
    }

    @Override
    public List<FtuElectlVolume> selectByMainId(String mainId) {
        return ftuElectlVolumeMapper.selectByMainId(mainId);
    }
}
