package org.jeecg.ftu.service;

import org.jeecg.ftu.entity.FtuWarnInfo;
import org.jeecg.ftu.entity.FtuF411Device;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 通信终端
 * @Author: jeecg-boot
 * @Date:   2025-06-23
 * @Version: V1.0
 */
public interface IFtuF411DeviceService extends IService<FtuF411Device> {

	public List<FtuF411Device> selectByMainId(String mainId);

	/**
	 * 添加一对多
	 *
	 * @param ftuF411Device
	 * @param ftuWarnInfoList
	 */
	public void saveMain(FtuF411Device ftuF411Device,List<FtuWarnInfo> ftuWarnInfoList) ;

	/**
	 * 修改一对多
	 *
   * @param ftuF411Device
   * @param ftuWarnInfoList
	 */
	public void updateMain(FtuF411Device ftuF411Device,List<FtuWarnInfo> ftuWarnInfoList);

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

}
