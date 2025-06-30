package org.jeecg.ftu.service.impl;

import org.jeecg.ftu.entity.FtuElectlVolume;
import org.jeecg.ftu.mapper.FtuElectlVolumeMapper;
import org.jeecg.ftu.service.IFtuElectlVolumeService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 电气量信息
 * @Author: jeecg-boot
 * @Date:   2025-06-23
 * @Version: V1.0
 */
@Service
public class FtuElectlVolumeServiceImpl extends ServiceImpl<FtuElectlVolumeMapper, FtuElectlVolume> implements IFtuElectlVolumeService {
	
	@Autowired
	private FtuElectlVolumeMapper ftuElectlVolumeMapper;
	
	@Override
	public List<FtuElectlVolume> selectByMainId(String mainId) {
		return ftuElectlVolumeMapper.selectByMainId(mainId);
	}
}
