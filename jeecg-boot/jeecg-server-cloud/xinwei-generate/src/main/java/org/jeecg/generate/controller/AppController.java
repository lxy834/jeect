package org.jeecg.generate.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.generate.service.IFdqOrderService;
import org.jeecg.generate.service.IFdqPropertyService;
import org.jeecg.generate.vo.AppStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author averice
 */
@RequestMapping("/generate/app")
@RestController
public class AppController {

    @Autowired
    private IFdqOrderService fdqOrderService;
    @Autowired
    private IFdqPropertyService fdqPropertyService;

    @Operation(summary = "获取应用信息")
    @GetMapping("/stat")
    public Result<AppStatVO> getStat() {
        return Result.OK(fdqOrderService.getStat());
    }

    @Operation(summary = "获取所有车辆车牌号")
    @GetMapping("/plate")
    public Result<List<String>> getPlate() {
        return Result.OK(fdqPropertyService.getPlate());
    }

}
