package org.jeecg.ftu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.ftu.entity.FtuElectlVolume;

import java.util.List;

/**
 * @Description: 电气量信息
 * @Author: jeecg-boot
 * @Date: 2025-06-23
 * @Version: V1.0
 */
public interface IFtuElectlVolumeService extends IService<FtuElectlVolume> {

    public void insertVolume(double v, double current, String ftuId, Integer sendMode);

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<FtuElectlVolume>
     */
    public List<FtuElectlVolume> selectByMainId(String mainId);

}
