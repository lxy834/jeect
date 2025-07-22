package org.jeecg.generate.service.impl;

import org.jeecg.generate.entity.FdqBasic;
import org.jeecg.generate.mapper.FdqBasicMapper;
import org.jeecg.generate.service.IFdqBasicService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 智慧终端
 * @Author: jeecg-boot
 * @Date:   2025-07-21
 * @Version: V1.0
 */
@Service
public class FdqBasicServiceImpl extends ServiceImpl<FdqBasicMapper, FdqBasic> implements IFdqBasicService {
	
	@Autowired
	private FdqBasicMapper fdqBasicMapper;
	
	@Override
	public List<FdqBasic> selectByMainId(String mainId) {
		return fdqBasicMapper.selectByMainId(mainId);
	}
}
