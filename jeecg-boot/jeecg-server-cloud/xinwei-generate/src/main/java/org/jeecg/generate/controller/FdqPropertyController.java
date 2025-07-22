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
import org.jeecg.generate.entity.FdqBasic;
import org.jeecg.generate.entity.FdqController;
import org.jeecg.generate.entity.FdqProperty;
import org.jeecg.generate.service.IFdqBasicService;
import org.jeecg.generate.service.IFdqControllerService;
import org.jeecg.generate.service.IFdqPropertyService;
import org.jeecg.generate.vo.FdqPropertyPage;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @Description: 应急装备
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Tag(name = "应急装备")
@RestController
@RequestMapping("/generate/fdqProperty")
@Slf4j
public class FdqPropertyController {
    @Autowired
    private IFdqPropertyService fdqPropertyService;
    @Autowired
    private IFdqBasicService fdqBasicService;
    @Autowired
    private IFdqControllerService fdqControllerService;

    /**
     * 分页列表查询
     *
     * @param fdqProperty
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "应急装备-分页列表查询")
    @Operation(summary = "应急装备-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<FdqProperty>> queryPageList(FdqProperty fdqProperty,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<FdqProperty> queryWrapper = QueryGenerator.initQueryWrapper(fdqProperty, req.getParameterMap());
        Page<FdqProperty> page = new Page<FdqProperty>(pageNo, pageSize);
        IPage<FdqProperty> pageList = fdqPropertyService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "根据车牌号查询统计数据")
    @GetMapping(value = "/index/stat/plate")
    public Result<Map<String, BigDecimal>> queryPlateList(@RequestParam("plate") String plate) {
        return Result.OK(fdqPropertyService.queryPlateList(plate));
    }

    /**
     * 添加
     *
     * @param fdqPropertyPage
     * @return
     */
    @AutoLog(value = "应急装备-添加")
    @Operation(summary = "应急装备-添加")
    @RequiresPermissions("generate:fdq_property:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody FdqPropertyPage fdqPropertyPage) {
        FdqProperty fdqProperty = new FdqProperty();
        BeanUtils.copyProperties(fdqPropertyPage, fdqProperty);
        fdqPropertyService.saveMain(fdqProperty, fdqPropertyPage.getFdqBasicList(), fdqPropertyPage.getFdqControllerList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param fdqPropertyPage
     * @return
     */
    @AutoLog(value = "应急装备-编辑")
    @Operation(summary = "应急装备-编辑")
    @RequiresPermissions("generate:fdq_property:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody FdqPropertyPage fdqPropertyPage) {
        FdqProperty fdqProperty = new FdqProperty();
        BeanUtils.copyProperties(fdqPropertyPage, fdqProperty);
        FdqProperty fdqPropertyEntity = fdqPropertyService.getById(fdqProperty.getId());
        if (fdqPropertyEntity == null) {
            return Result.error("未找到对应数据");
        }
        fdqPropertyService.updateMain(fdqProperty, fdqPropertyPage.getFdqBasicList(), fdqPropertyPage.getFdqControllerList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "应急装备-通过id删除")
    @Operation(summary = "应急装备-通过id删除")
    @RequiresPermissions("generate:fdq_property:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        fdqPropertyService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "应急装备-批量删除")
    @Operation(summary = "应急装备-批量删除")
    @RequiresPermissions("generate:fdq_property:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.fdqPropertyService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "应急装备-通过id查询")
    @Operation(summary = "应急装备-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<FdqProperty> queryById(@RequestParam(name = "id", required = true) String id) {
        FdqProperty fdqProperty = fdqPropertyService.getById(id);
        if (fdqProperty == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(fdqProperty);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "智慧终端通过主表ID查询")
    @Operation(summary = "智慧终端主表ID查询")
    @GetMapping(value = "/queryFdqBasicByMainId")
    public Result<List<FdqBasic>> queryFdqBasicListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<FdqBasic> fdqBasicList = fdqBasicService.selectByMainId(id);
        return Result.OK(fdqBasicList);
    }

    /**
     * 通过id查询
     *
     * @param plateNumber
     * @return
     */
    //@AutoLog(value = "历史数据通过主表ID查询")
    @Operation(summary = "历史数据主表ID查询")
    @GetMapping(value = "/queryFdqControllerByMainId")
    public Result<List<FdqController>> queryFdqControllerListByMainId(@RequestParam(name = "plateNumber", required = true) String plateNumber) {
        List<FdqController> fdqControllerList = fdqControllerService.selectByMainId(plateNumber);
        return Result.OK(fdqControllerList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param fdqProperty
     */
    @RequiresPermissions("generate:fdq_property:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FdqProperty fdqProperty) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<FdqProperty> queryWrapper = QueryGenerator.initQueryWrapper(fdqProperty, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //配置选中数据查询条件
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        //Step.2 获取导出数据
        List<FdqProperty> fdqPropertyList = fdqPropertyService.list(queryWrapper);

        // Step.3 组装pageList
        List<FdqPropertyPage> pageList = new ArrayList<FdqPropertyPage>();
        for (FdqProperty main : fdqPropertyList) {
            FdqPropertyPage vo = new FdqPropertyPage();
            BeanUtils.copyProperties(main, vo);
            List<FdqBasic> fdqBasicList = fdqBasicService.selectByMainId(main.getId());
            vo.setFdqBasicList(fdqBasicList);
            List<FdqController> fdqControllerList = fdqControllerService.selectByMainId(main.getId());
            vo.setFdqControllerList(fdqControllerList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "应急装备列表");
        mv.addObject(NormalExcelConstants.CLASS, FdqPropertyPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("应急装备数据", "导出人:" + sysUser.getRealname(), "应急装备"));
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
    @RequiresPermissions("generate:fdq_property:importExcel")
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
                List<FdqPropertyPage> list = ExcelImportUtil.importExcel(file.getInputStream(), FdqPropertyPage.class, params);
                for (FdqPropertyPage page : list) {
                    FdqProperty po = new FdqProperty();
                    BeanUtils.copyProperties(page, po);
                    fdqPropertyService.saveMain(po, page.getFdqBasicList(), page.getFdqControllerList());
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
