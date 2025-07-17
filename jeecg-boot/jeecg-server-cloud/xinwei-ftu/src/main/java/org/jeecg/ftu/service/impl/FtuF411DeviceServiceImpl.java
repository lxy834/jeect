package org.jeecg.ftu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.ftu.entity.FtuF411Device;
import org.jeecg.ftu.entity.FtuWarnInfo;
import org.jeecg.ftu.mapper.FtuF411DeviceMapper;
import org.jeecg.ftu.mapper.FtuWarnInfoMapper;
import org.jeecg.ftu.service.IFtuF411DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 通信终端
 * @Author: jeecg-boot
 * @Date: 2025-06-23
 * @Version: V1.0
 */
@Service
public class FtuF411DeviceServiceImpl extends ServiceImpl<FtuF411DeviceMapper, FtuF411Device> implements IFtuF411DeviceService {

    @Autowired
    private FtuF411DeviceMapper ftuF411DeviceMapper;
    @Autowired
    private FtuWarnInfoMapper ftuWarnInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(FtuF411Device ftuF411Device, List<FtuWarnInfo> ftuWarnInfoList) {
        ftuF411DeviceMapper.insert(ftuF411Device);
        if (ftuWarnInfoList != null && ftuWarnInfoList.size() > 0) {
            for (FtuWarnInfo entity : ftuWarnInfoList) {
                //外键设置
                entity.setDeviceId(ftuF411Device.getId());
                ftuWarnInfoMapper.insert(entity);
            }
        }
    }


    @Override
    public List<FtuF411Device> selectByMainId(String mainId) {
        return ftuF411DeviceMapper.selectByMainId(mainId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(FtuF411Device ftuF411Device, List<FtuWarnInfo> ftuWarnInfoList) {
        ftuF411DeviceMapper.updateById(ftuF411Device);

        //1.先删除子表数据
        ftuWarnInfoMapper.deleteByMainId(ftuF411Device.getId());

        //2.子表数据重新插入
        if (ftuWarnInfoList != null && ftuWarnInfoList.size() > 0) {
            for (FtuWarnInfo entity : ftuWarnInfoList) {
                //外键设置
                entity.setDeviceId(ftuF411Device.getId());
                ftuWarnInfoMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        ftuWarnInfoMapper.deleteByMainId(id);
        ftuF411DeviceMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            ftuWarnInfoMapper.deleteByMainId(id.toString());
            ftuF411DeviceMapper.deleteById(id);
        }
    }

    @Override
    public FtuF411Device get411Device(String card) {
        return baseMapper.get411Device(card);
    }

    @Override
    public boolean updateMode(Integer mode, String id) {
        return baseMapper.updateMode(mode, id);
    }

}
