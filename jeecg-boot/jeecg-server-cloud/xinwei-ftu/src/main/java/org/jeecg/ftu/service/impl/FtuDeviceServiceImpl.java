package org.jeecg.ftu.service.impl;

import org.jeecg.ftu.entity.FtuDevice;
import org.jeecg.ftu.entity.FtuF411Device;
import org.jeecg.ftu.entity.FtuElectlVolume;
import org.jeecg.ftu.entity.FtuWarnInfo;
import org.jeecg.ftu.mapper.FtuF411DeviceMapper;
import org.jeecg.ftu.mapper.FtuElectlVolumeMapper;
import org.jeecg.ftu.mapper.FtuWarnInfoMapper;
import org.jeecg.ftu.mapper.FtuDeviceMapper;
import org.jeecg.ftu.service.IFtuDeviceService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: FTU终端
 * @Author: jeecg-boot
 * @Date:   2025-06-23
 * @Version: V1.0
 */
@Service
public class FtuDeviceServiceImpl extends ServiceImpl<FtuDeviceMapper, FtuDevice> implements IFtuDeviceService {

	@Autowired
	private FtuDeviceMapper ftuDeviceMapper;
	@Autowired
	private FtuF411DeviceMapper ftuF411DeviceMapper;
	@Autowired
	private FtuElectlVolumeMapper ftuElectlVolumeMapper;
	@Autowired
	private FtuWarnInfoMapper ftuWarnInfoMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(FtuDevice ftuDevice, List<FtuF411Device> ftuF411DeviceList,List<FtuElectlVolume> ftuElectlVolumeList,List<FtuWarnInfo> ftuWarnInfoList) {
		ftuDeviceMapper.insert(ftuDevice);
		if(ftuF411DeviceList!=null && !ftuF411DeviceList.isEmpty()) {
			for(FtuF411Device entity:ftuF411DeviceList) {
				//外键设置
				entity.setFtuId(ftuDevice.getId());
				ftuF411DeviceMapper.insert(entity);
			}
		}
		if(ftuElectlVolumeList!=null && !ftuElectlVolumeList.isEmpty()) {
			for(FtuElectlVolume entity:ftuElectlVolumeList) {
				//外键设置
				entity.setFtuId(ftuDevice.getId());
				ftuElectlVolumeMapper.insert(entity);
			}
		}
		if(ftuWarnInfoList!=null && !ftuWarnInfoList.isEmpty()) {
			for(FtuWarnInfo entity:ftuWarnInfoList) {
				//外键设置
				entity.setDeviceId(ftuDevice.getId());
				ftuWarnInfoMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(FtuDevice ftuDevice,List<FtuF411Device> ftuF411DeviceList,List<FtuElectlVolume> ftuElectlVolumeList,List<FtuWarnInfo> ftuWarnInfoList) {
		ftuDeviceMapper.updateById(ftuDevice);

		//1.先删除子表数据
		ftuF411DeviceMapper.deleteByMainId(ftuDevice.getId());
		ftuElectlVolumeMapper.deleteByMainId(ftuDevice.getId());
		ftuWarnInfoMapper.deleteByMainId(ftuDevice.getId());

		//2.子表数据重新插入
		if(ftuF411DeviceList!=null && !ftuF411DeviceList.isEmpty()) {
			for(FtuF411Device entity:ftuF411DeviceList) {
				//外键设置
				entity.setFtuId(ftuDevice.getId());
				ftuF411DeviceMapper.insert(entity);
			}
		}
		if(ftuElectlVolumeList!=null && !ftuElectlVolumeList.isEmpty()) {
			for(FtuElectlVolume entity:ftuElectlVolumeList) {
				//外键设置
				entity.setFtuId(ftuDevice.getId());
				ftuElectlVolumeMapper.insert(entity);
			}
		}
		if(ftuWarnInfoList!=null && !ftuWarnInfoList.isEmpty()) {
			for(FtuWarnInfo entity:ftuWarnInfoList) {
				//外键设置
				entity.setDeviceId(ftuDevice.getId());
				ftuWarnInfoMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		ftuF411DeviceMapper.deleteByMainId(id);
		ftuElectlVolumeMapper.deleteByMainId(id);
		ftuWarnInfoMapper.deleteByMainId(id);
		ftuDeviceMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			ftuF411DeviceMapper.deleteByMainId(id.toString());
			ftuElectlVolumeMapper.deleteByMainId(id.toString());
			ftuWarnInfoMapper.deleteByMainId(id.toString());
			ftuDeviceMapper.deleteById(id);
		}
	}

}
