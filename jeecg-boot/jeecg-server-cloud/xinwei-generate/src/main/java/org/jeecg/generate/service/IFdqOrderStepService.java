package org.jeecg.generate.service;

import org.jeecg.generate.entity.FdqOrderStep;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 任务详情
 * @Author: jeecg-boot
 * @Date:   2025-07-21
 * @Version: V1.0
 */
public interface IFdqOrderStepService extends IService<FdqOrderStep> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<FdqOrderStep>
	 */
	public List<FdqOrderStep> selectByMainId(String mainId);
}
