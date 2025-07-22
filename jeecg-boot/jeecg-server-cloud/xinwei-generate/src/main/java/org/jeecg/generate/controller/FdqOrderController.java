package org.jeecg.generate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.generate.dto.FdqOrderDTO;
import org.jeecg.generate.entity.FdqOrder;
import org.jeecg.generate.entity.FdqOrderStep;
import org.jeecg.generate.service.IFdqOrderService;
import org.jeecg.generate.service.IFdqOrderStepService;
import org.jeecg.generate.vo.FdqOrderPage;
import org.jeecg.generate.vo.IndexOrderVO;
import org.jeecg.generate.vo.IndexStatVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @Description: 工单台账
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Tag(name = "工单台账")
@RestController
@RequestMapping("/generate/fdqOrder")
@Slf4j
public class FdqOrderController {
    @Autowired
    private IFdqOrderService fdqOrderService;
    @Autowired
    private IFdqOrderStepService fdqOrderStepService;

    /**
     * 分页列表查询
     *
     * @param fdqOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "工单台账-分页列表查询")
    @Operation(summary = "工单台账-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<FdqOrder>> queryPageList(FdqOrder fdqOrder,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {
        QueryWrapper<FdqOrder> queryWrapper = QueryGenerator.initQueryWrapper(fdqOrder, req.getParameterMap());
        Page<FdqOrder> page = new Page<FdqOrder>(pageNo, pageSize);
        IPage<FdqOrder> pageList = fdqOrderService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "首页左侧工单详细信息")
    @GetMapping(value = "/index/order")
    public Result<IndexOrderVO> indexOrder(FdqOrderDTO fdqOrderPage) {
        return Result.OK(fdqOrderService.indexOrder(fdqOrderPage));
    }

    @Operation(summary = "首页右侧统计数据")
    @GetMapping(value = "/index/stat")
    public Result<IndexStatVO> statVOResult() {
        return Result.OK(fdqOrderService.statVOResult());
    }

    /**
     * 添加
     *
     * @param fdqOrderPage
     * @return
     */
    @AutoLog(value = "工单台账-添加")
    @Operation(summary = "工单台账-添加")
    @RequiresPermissions("generate:fdq_order:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody FdqOrderPage fdqOrderPage) {
        FdqOrder fdqOrder = new FdqOrder();
        BeanUtils.copyProperties(fdqOrderPage, fdqOrder);
        fdqOrderService.saveMain(fdqOrder, fdqOrderPage.getFdqOrderStepList());
        return Result.OK("添加成功！");
    }


    /**
     * 编辑
     *
     * @param fdqOrderPage
     * @return
     */
    @AutoLog(value = "工单台账-编辑")
    @Operation(summary = "工单台账-编辑")
    @RequiresPermissions("generate:fdq_order:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody FdqOrderPage fdqOrderPage) {
        FdqOrder fdqOrder = new FdqOrder();
        BeanUtils.copyProperties(fdqOrderPage, fdqOrder);
        FdqOrder fdqOrderEntity = fdqOrderService.getById(fdqOrder.getId());
        if (fdqOrderEntity == null) {
            return Result.error("未找到对应数据");
        }
        fdqOrderService.updateMain(fdqOrder, fdqOrderPage.getFdqOrderStepList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "工单台账-通过id删除")
    @Operation(summary = "工单台账-通过id删除")
    @RequiresPermissions("generate:fdq_order:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        fdqOrderService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "工单台账-批量删除")
    @Operation(summary = "工单台账-批量删除")
    @RequiresPermissions("generate:fdq_order:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.fdqOrderService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "工单台账-通过id查询")
    @Operation(summary = "工单台账-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<FdqOrder> queryById(@RequestParam(name = "id", required = true) String id) {
        FdqOrder fdqOrder = fdqOrderService.getById(id);
        if (fdqOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(fdqOrder);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "任务详情通过主表ID查询")
    @Operation(summary = "任务详情主表ID查询")
    @GetMapping(value = "/queryFdqOrderStepByMainId")
    public Result<List<FdqOrderStep>> queryFdqOrderStepListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<FdqOrderStep> fdqOrderStepList = fdqOrderStepService.selectByMainId(id);
        return Result.OK(fdqOrderStepList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param fdqOrder
     */
    @RequiresPermissions("generate:fdq_order:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FdqOrder fdqOrder) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<FdqOrder> queryWrapper = QueryGenerator.initQueryWrapper(fdqOrder, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //配置选中数据查询条件
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        //Step.2 获取导出数据
        List<FdqOrder> fdqOrderList = fdqOrderService.list(queryWrapper);

        // Step.3 组装pageList
        List<FdqOrderPage> pageList = new ArrayList<FdqOrderPage>();
        for (FdqOrder main : fdqOrderList) {
            FdqOrderPage vo = new FdqOrderPage();
            BeanUtils.copyProperties(main, vo);
            List<FdqOrderStep> fdqOrderStepList = fdqOrderStepService.selectByMainId(main.getId());
            vo.setFdqOrderStepList(fdqOrderStepList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "工单台账列表");
        mv.addObject(NormalExcelConstants.CLASS, FdqOrderPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("工单台账数据", "导出人:" + sysUser.getRealname(), "工单台账"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("generate:fdq_order:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<FdqOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), FdqOrderPage.class, params);
                for (FdqOrderPage page : list) {
                    FdqOrder po = new FdqOrder();
                    BeanUtils.copyProperties(page, po);
                    fdqOrderService.saveMain(po, page.getFdqOrderStepList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

}
