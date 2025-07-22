package org.jeecg.generate.service;

import org.jeecg.generate.entity.FdqController;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 历史数据
 * @Author: jeecg-boot
 * @Date:   2025-07-21
 * @Version: V1.0
 */
public interface IFdqControllerService extends IService<FdqController> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<FdqController>
	 */
	public List<FdqController> selectByMainId(String mainId);
}
