package org.jeecg.ftu.service;

import org.jeecg.ftu.entity.FtuElectlVolume;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 电气量信息
 * @Author: jeecg-boot
 * @Date:   2025-06-23
 * @Version: V1.0
 */
public interface IFtuElectlVolumeService extends IService<FtuElectlVolume> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<FtuElectlVolume>
	 */
	public List<FtuElectlVolume> selectByMainId(String mainId);
}
