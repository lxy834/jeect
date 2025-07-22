package org.jeecg.generate.service.impl;

import org.jeecg.generate.entity.FdqController;
import org.jeecg.generate.mapper.FdqControllerMapper;
import org.jeecg.generate.service.IFdqControllerService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 历史数据
 * @Author: jeecg-boot
 * @Date:   2025-07-21
 * @Version: V1.0
 */
@Service
public class FdqControllerServiceImpl extends ServiceImpl<FdqControllerMapper, FdqController> implements IFdqControllerService {
	
	@Autowired
	private FdqControllerMapper fdqControllerMapper;
	
	@Override
	public List<FdqController> selectByMainId(String mainId) {
		return fdqControllerMapper.selectByMainId(mainId);
	}
}
