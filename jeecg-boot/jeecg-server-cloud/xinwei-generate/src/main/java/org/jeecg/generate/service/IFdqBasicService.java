package org.jeecg.generate.service;

import org.jeecg.generate.entity.FdqBasic;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 智慧终端
 * @Author: jeecg-boot
 * @Date:   2025-07-21
 * @Version: V1.0
 */
public interface IFdqBasicService extends IService<FdqBasic> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<FdqBasic>
	 */
	public List<FdqBasic> selectByMainId(String mainId);
}
