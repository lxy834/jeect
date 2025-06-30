package org.jeecg.ftu.service;

import org.jeecg.ftu.entity.FtuWarnInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 告警记录
 * @Author: jeecg-boot
 * @Date:   2025-06-30
 * @Version: V1.0
 */
public interface IFtuWarnInfoService extends IService<FtuWarnInfo> {

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<FtuWarnInfo>
     */
    public List<FtuWarnInfo> selectByMainId(String mainId);
}
