package org.jeecg.ftu.service.impl;

import org.jeecg.ftu.entity.FtuWarnInfo;
import org.jeecg.ftu.mapper.FtuWarnInfoMapper;
import org.jeecg.ftu.service.IFtuWarnInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 告警记录
 * @Author: jeecg-boot
 * @Date:   2025-06-30
 * @Version: V1.0
 */
@Service
public class FtuWarnInfoServiceImpl extends ServiceImpl<FtuWarnInfoMapper, FtuWarnInfo> implements IFtuWarnInfoService {

    @Autowired
    private FtuWarnInfoMapper ftuWarnInfoMapper;

    @Override
    public List<FtuWarnInfo> selectByMainId(String mainId) {
        return ftuWarnInfoMapper.selectByMainId(mainId);
    }
}
