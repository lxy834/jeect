package org.jeecg.generate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.generate.entity.FdqBasic;
import org.jeecg.generate.entity.FdqController;
import org.jeecg.generate.entity.FdqProperty;
import org.jeecg.generate.mapper.FdqBasicMapper;
import org.jeecg.generate.mapper.FdqControllerMapper;
import org.jeecg.generate.mapper.FdqPropertyMapper;
import org.jeecg.generate.service.IFdqPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 应急装备
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Service
public class FdqPropertyServiceImpl extends ServiceImpl<FdqPropertyMapper, FdqProperty> implements IFdqPropertyService {

    @Autowired
    private FdqPropertyMapper fdqPropertyMapper;
    @Autowired
    private FdqBasicMapper fdqBasicMapper;
    @Autowired
    private FdqControllerMapper fdqControllerMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(FdqProperty fdqProperty, List<FdqBasic> fdqBasicList, List<FdqController> fdqControllerList) {
        fdqPropertyMapper.insert(fdqProperty);
        if (fdqBasicList != null && fdqBasicList.size() > 0) {
            for (FdqBasic entity : fdqBasicList) {
                //外键设置
                entity.setPlateNumber(fdqProperty.getPlateNumber());
                fdqBasicMapper.insert(entity);
            }
        }
        if (fdqControllerList != null && fdqControllerList.size() > 0) {
            for (FdqController entity : fdqControllerList) {
                //外键设置
                entity.setPlateNumber(fdqProperty.getPlateNumber());
                fdqControllerMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(FdqProperty fdqProperty, List<FdqBasic> fdqBasicList, List<FdqController> fdqControllerList) {
        fdqPropertyMapper.updateById(fdqProperty);

        //1.先删除子表数据
        fdqBasicMapper.deleteByMainId(fdqProperty.getId());
        fdqControllerMapper.deleteByMainId(fdqProperty.getId());

        //2.子表数据重新插入
        if (fdqBasicList != null && fdqBasicList.size() > 0) {
            for (FdqBasic entity : fdqBasicList) {
                //外键设置
                entity.setPlateNumber(fdqProperty.getPlateNumber());
                fdqBasicMapper.insert(entity);
            }
        }
        if (fdqControllerList != null && fdqControllerList.size() > 0) {
            for (FdqController entity : fdqControllerList) {
                //外键设置
                entity.setPlateNumber(fdqProperty.getPlateNumber());
                fdqControllerMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        fdqBasicMapper.deleteByMainId(id);
        fdqControllerMapper.deleteByMainId(id);
        fdqPropertyMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            fdqBasicMapper.deleteByMainId(id.toString());
            fdqControllerMapper.deleteByMainId(id.toString());
            fdqPropertyMapper.deleteById(id);
        }
    }

    @Override
    public Map<String, BigDecimal> queryPlateList(String plate) {
        return baseMapper.selectCountKwh(plate);
    }

}
