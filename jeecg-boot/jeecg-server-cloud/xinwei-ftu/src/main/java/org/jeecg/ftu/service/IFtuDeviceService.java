package org.jeecg.ftu.service;

import org.jeecg.ftu.entity.FtuF411Device;
import org.jeecg.ftu.entity.FtuElectlVolume;
import org.jeecg.ftu.entity.FtuWarnInfo;
import org.jeecg.ftu.entity.FtuDevice;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.ftu.vo.FtuElectlVolumeVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: FTU终端
 * @Author: jeecg-boot
 * @Date:   2025-06-23
 * @Version: V1.0
 */
public interface IFtuDeviceService extends IService<FtuDevice> {

	/**
	 * 添加一对多
	 *
	 * @param ftuDevice
	 * @param ftuF411DeviceList
	 * @param ftuElectlVolumeList
	 * @param ftuWarnInfoList
	 */
	public void saveMain(FtuDevice ftuDevice,List<FtuF411Device> ftuF411DeviceList,List<FtuElectlVolume> ftuElectlVolumeList,List<FtuWarnInfo> ftuWarnInfoList) ;

	/**
	 * 修改一对多
	 *
   * @param ftuDevice
   * @param ftuF411DeviceList
   * @param ftuElectlVolumeList
   * @param ftuWarnInfoList
	 */
	public void updateMain(FtuDevice ftuDevice,List<FtuF411Device> ftuF411DeviceList,List<FtuElectlVolume> ftuElectlVolumeList,List<FtuWarnInfo> ftuWarnInfoList);

	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	List<FtuElectlVolumeVO> queryFtuElectlVolumeList();


}
