package org.jeecg.generate.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.DateUtils;
import org.jeecg.generate.entity.FdqHeartBeat;
import org.jeecg.generate.service.IFdqHeartBeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;

/**
 * @Description: 系统
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Tag(name = "系统")
@RestController
@RequestMapping("/generate/fdqHeartBeat")
@Slf4j
public class FdqHeartBeatController extends JeecgController<FdqHeartBeat, IFdqHeartBeatService> {
    @Autowired
    private IFdqHeartBeatService fdqHeartBeatService;

    /**
     * 分页列表查询
     *
     * @param fdqHeartBeat
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "系统-分页列表查询")
    @Operation(summary = "系统-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<FdqHeartBeat>> queryPageList(FdqHeartBeat fdqHeartBeat,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest req) {
        // 获取当前日期的开始时间
        Date start = DateUtils.getStartOfDay();
// 获取当前日期的结束时间
        Date end = DateUtils.getEndOfDay();
        Page<FdqHeartBeat> page = new Page<FdqHeartBeat>(pageNo, pageSize);
        IPage<FdqHeartBeat> pageList = fdqHeartBeatService.page(page, Wrappers.<FdqHeartBeat>lambdaQuery()
                .between(FdqHeartBeat::getCreateTime, start, end).orderByDesc(FdqHeartBeat::getCreateTime));
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param fdqHeartBeat
     * @return
     */
    @AutoLog(value = "系统-添加")
    @Operation(summary = "系统-添加")
    @RequiresPermissions("generate:fdq_heart_beat:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody FdqHeartBeat fdqHeartBeat) {
        fdqHeartBeatService.save(fdqHeartBeat);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param fdqHeartBeat
     * @return
     */
    @AutoLog(value = "系统-编辑")
    @Operation(summary = "系统-编辑")
    @RequiresPermissions("generate:fdq_heart_beat:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody FdqHeartBeat fdqHeartBeat) {
        fdqHeartBeatService.updateById(fdqHeartBeat);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统-通过id删除")
    @Operation(summary = "系统-通过id删除")
    @RequiresPermissions("generate:fdq_heart_beat:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        fdqHeartBeatService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "系统-批量删除")
    @Operation(summary = "系统-批量删除")
    @RequiresPermissions("generate:fdq_heart_beat:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.fdqHeartBeatService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "系统-通过id查询")
    @Operation(summary = "系统-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<FdqHeartBeat> queryById(@RequestParam(name = "id", required = true) String id) {
        FdqHeartBeat fdqHeartBeat = fdqHeartBeatService.getById(id);
        if (fdqHeartBeat == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(fdqHeartBeat);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param fdqHeartBeat
     */
    @RequiresPermissions("generate:fdq_heart_beat:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FdqHeartBeat fdqHeartBeat) {
        return super.exportXls(request, fdqHeartBeat, FdqHeartBeat.class, "系统");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("generate:fdq_heart_beat:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FdqHeartBeat.class);
    }

}
