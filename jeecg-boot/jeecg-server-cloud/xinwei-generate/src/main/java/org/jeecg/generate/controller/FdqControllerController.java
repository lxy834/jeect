package org.jeecg.generate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.generate.entity.FdqController;
import org.jeecg.generate.service.IFdqControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 中控器数据
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Tag(name = "中控器数据")
@RestController
@RequestMapping("/generate/ftuController")
@Slf4j
public class FdqControllerController extends JeecgController<FdqController, IFdqControllerService> {
    @Autowired
    private IFdqControllerService ftuControllerService;

    /**
     * 分页列表查询
     *
     * @param ftuController
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "中控器数据-分页列表查询")
    @Operation(summary = "中控器数据-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<FdqController>> queryPageList(FdqController ftuController,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        QueryWrapper<FdqController> queryWrapper = QueryGenerator.initQueryWrapper(ftuController, req.getParameterMap());
        Page<FdqController> page = new Page<FdqController>(pageNo, pageSize);
        IPage<FdqController> pageList = ftuControllerService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param ftuController
     * @return
     */
    @AutoLog(value = "中控器数据-添加")
    @Operation(summary = "中控器数据-添加")
    @RequiresPermissions("generate:ftu_controller:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody FdqController ftuController) {
        ftuControllerService.save(ftuController);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param ftuController
     * @return
     */
    @AutoLog(value = "中控器数据-编辑")
    @Operation(summary = "中控器数据-编辑")
    @RequiresPermissions("generate:ftu_controller:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody FdqController ftuController) {
        ftuControllerService.updateById(ftuController);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "中控器数据-通过id删除")
    @Operation(summary = "中控器数据-通过id删除")
    @RequiresPermissions("generate:ftu_controller:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        ftuControllerService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "中控器数据-批量删除")
    @Operation(summary = "中控器数据-批量删除")
    @RequiresPermissions("generate:ftu_controller:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.ftuControllerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "中控器数据-通过id查询")
    @Operation(summary = "中控器数据-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<FdqController> queryById(@RequestParam(name = "id", required = true) String id) {
        FdqController ftuController = ftuControllerService.getById(id);
        if (ftuController == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(ftuController);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param ftuController
     */
    @RequiresPermissions("generate:ftu_controller:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FdqController ftuController) {
        return super.exportXls(request, ftuController, FdqController.class, "中控器数据");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("generate:ftu_controller:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FdqController.class);
    }

}
