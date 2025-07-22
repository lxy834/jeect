package org.jeecg.generate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.generate.dto.FdqTrackDTO;
import org.jeecg.generate.entity.FdqTrack;
import org.jeecg.generate.service.IFdqTrackService;
import org.jeecg.generate.vo.FdqTrackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 定位数据
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Tag(name = "定位数据")
@RestController
@RequestMapping("/generate/fdqTrack")
@Slf4j
public class FdqTrackController extends JeecgController<FdqTrack, IFdqTrackService> {

    @Autowired
    private IFdqTrackService fdqTrackService;

    @Operation(summary = "根据设备编号或时间节点获取轨迹信息")
    @PostMapping("/getTrackByPlate")
    public Result<List<FdqTrackVO>> getTrackByPlate(@RequestBody FdqTrackDTO trackDTO) {
        return Result.OK(fdqTrackService.getTrackByPlate(trackDTO));
    }

}
