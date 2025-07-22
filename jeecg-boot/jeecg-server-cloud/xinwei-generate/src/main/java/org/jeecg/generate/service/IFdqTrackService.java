package org.jeecg.generate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.generate.dto.FdqTrackDTO;
import org.jeecg.generate.entity.FdqTrack;
import org.jeecg.generate.vo.FdqTrackVO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description: 定位数据
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
public interface IFdqTrackService extends IService<FdqTrack> {
    List<FdqTrackVO> getTrackByPlate(@RequestBody FdqTrackDTO trackDTO);
}
