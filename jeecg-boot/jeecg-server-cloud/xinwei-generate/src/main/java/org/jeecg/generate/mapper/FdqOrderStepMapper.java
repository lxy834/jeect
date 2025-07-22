package org.jeecg.generate.mapper;

import java.util.List;
import org.jeecg.generate.entity.FdqOrderStep;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 任务详情
 * @Author: jeecg-boot
 * @Date:   2025-07-21
 * @Version: V1.0
 */
public interface FdqOrderStepMapper extends BaseMapper<FdqOrderStep> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<FdqOrderStep>
   */
	public List<FdqOrderStep> selectByMainId(@Param("mainId") String mainId);
}
