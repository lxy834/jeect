package org.jeecg.ftu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.util.RestUtil;
import org.jeecg.ftu.entity.FtuDevice;
import org.jeecg.ftu.entity.FtuF411Device;
import org.jeecg.ftu.entity.FtuElectlVolume;
import org.jeecg.ftu.entity.FtuWarnInfo;
import org.jeecg.ftu.mapper.FtuF411DeviceMapper;
import org.jeecg.ftu.mapper.FtuElectlVolumeMapper;
import org.jeecg.ftu.mapper.FtuWarnInfoMapper;
import org.jeecg.ftu.mapper.FtuDeviceMapper;
import org.jeecg.ftu.service.IFtuDeviceService;
import org.jeecg.ftu.vo.FtuElectlVolumeVO;
import org.jeecg.ftu.vo.IotDevice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

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


	public static HttpHeaders getHeaders(String token) {
		HttpHeaders headers = new HttpHeaders();
		String mediaType = MediaType.APPLICATION_JSON_VALUE;
		headers.setContentType(MediaType.parseMediaType(mediaType));
		headers.set("Accept", mediaType);
		headers.set("X-Token", token);
		return headers;
	}

	public static void savaIot(FtuF411Device device,String ip){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username","ftuAppUser");
		jsonObject.put("password", "qwer1234Q@");
		JSONObject post = RestUtil.post("http://192.168.1.204:8848/base/api/login",jsonObject);
		HttpHeaders headers = getHeaders(post.getJSONObject("data").getString("token"));
		JSONObject deviceJson = new JSONObject();
		deviceJson.put("deviceName",device.getDeviceName());
		deviceJson.put("deviceSn",device.getDeviceCode());
		deviceJson.put("ipAddr",ip);
		deviceJson.put("deviceType","direct");
		deviceJson.put("blgProductId",3);
		deviceJson.put("online","n");
		deviceJson.put("onlineBool",true);
		deviceJson.put("allowAutomatic","n");
		deviceJson.put("allowAutomaticBool",false);
		deviceJson.put("blgProductName","F411");
		deviceJson.put("categoryCode","1");
		deviceJson.put("enable","y");
		deviceJson.put("useScenario","ftu");
		RestUtil.request("http://192.168.1.204:8848/device/createBaseDevice", HttpMethod.POST, headers, null, deviceJson, JSONObject.class);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(FtuDevice ftuDevice, List<FtuF411Device> ftuF411DeviceList,List<FtuElectlVolume> ftuElectlVolumeList,List<FtuWarnInfo> ftuWarnInfoList) {
		ftuDeviceMapper.insert(ftuDevice);
		if(ftuF411DeviceList!=null && !ftuF411DeviceList.isEmpty()) {
			for(FtuF411Device entity:ftuF411DeviceList) {
				//外键设置
				entity.setFtuId(ftuDevice.getId());
				entity.setDeviceCode("FTU"+ftuDevice.getIp().replace(".",""));
				savaIot(entity,ftuDevice.getIp());
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
//				savaIot(entity,ftuDevice.getIp());
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

	@Override
	public List<FtuElectlVolumeVO> queryFtuElectlVolumeList() {
		return baseMapper.queryFtuElectlVolumeList();
	}

	@Override
	public Map<String, Object> getVolumeStatByFtuId(String ftuId) {
		List<FtuElectlVolume> volumeList = ftuElectlVolumeMapper.selectList(Wrappers.<FtuElectlVolume>query().lambda().eq(FtuElectlVolume::getFtuId, ftuId));

		int size = volumeList.size();
		Map<String, Object> resultMap = new HashMap<>(5);

		List<String> createTimeList = new ArrayList<>(size);
		List<Double> activePowerList = new ArrayList<>(size);
		List<Double> factorList = new ArrayList<>(size);
		List<Double> voltageList = new ArrayList<>(size);
		List<Double> ftuCurrentList = new ArrayList<>(size);

		// 使用ThreadLocal确保SimpleDateFormat线程安全
		ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(
				() -> new SimpleDateFormat("MM/dd HH:mm")
		);

		for (FtuElectlVolume volume : volumeList) {
			// 格式化日期
			Date createTime = volume.getCreateTime();
			createTimeList.add(createTime != null ?
					dateFormat.get().format(createTime) : null);

			activePowerList.add(volume.getActivePower());
			factorList.add(volume.getFactor());
			voltageList.add(volume.getVoltage());
			ftuCurrentList.add(volume.getFtuCurrent());
		}

		resultMap.put("createTimeList", createTimeList);
		resultMap.put("activePowerList", activePowerList);
		resultMap.put("factorList", factorList);
		resultMap.put("voltageList", voltageList);
		resultMap.put("ftuCurrentList", ftuCurrentList);

		return resultMap;
	}

}
