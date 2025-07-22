package org.jeecg.generate.mapper;

import java.util.List;
import org.jeecg.generate.entity.FdqBasic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 智慧终端
 * @Author: jeecg-boot
 * @Date:   2025-07-21
 * @Version: V1.0
 */
public interface FdqBasicMapper extends BaseMapper<FdqBasic> {

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
   * @return List<FdqBasic>
   */
	public List<FdqBasic> selectByMainId(@Param("mainId") String mainId);
}
