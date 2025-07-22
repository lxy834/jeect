package org.jeecg.generate.service.impl;

import org.jeecg.generate.entity.FdqOrderStep;
import org.jeecg.generate.mapper.FdqOrderStepMapper;
import org.jeecg.generate.service.IFdqOrderStepService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 任务详情
 * @Author: jeecg-boot
 * @Date:   2025-07-21
 * @Version: V1.0
 */
@Service
public class FdqOrderStepServiceImpl extends ServiceImpl<FdqOrderStepMapper, FdqOrderStep> implements IFdqOrderStepService {
	
	@Autowired
	private FdqOrderStepMapper fdqOrderStepMapper;
	
	@Override
	public List<FdqOrderStep> selectByMainId(String mainId) {
		return fdqOrderStepMapper.selectByMainId(mainId);
	}
}
