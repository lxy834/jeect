package org.jeecg.ftu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.ftu.entity.FtuF411Device;
import org.jeecg.ftu.entity.FtuWarnInfo;
import org.jeecg.ftu.service.IFtuF411DeviceService;
import org.jeecg.ftu.service.IFtuWarnInfoService;
import org.jeecg.ftu.vo.FtuF411DevicePage;
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
import java.util.*;


/**
 * @Description: 通信终端
 * @Author: jeecg-boot
 * @Date: 2025-06-23
 * @Version: V1.0
 */
@Tag(name = "通信终端")
@RestController
@RequestMapping("/ftu/ftuF411Device")
@Slf4j
public class FtuF411DeviceController {
    @Autowired
    private IFtuF411DeviceService ftuF411DeviceService;
    @Autowired
    private IFtuWarnInfoService ftuWarnInfoService;

    /**
     * 分页列表查询
     *
     * @param ftuF411Device
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "通信终端-分页列表查询")
    @Operation(summary = "通信终端-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<FtuF411Device>> queryPageList(FtuF411Device ftuF411Device,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        // 自定义查询规则
        Map<String, QueryRuleEnum> customeRuleMap = new HashMap<>();
        // 自定义多选的查询规则为：LIKE_WITH_OR
        customeRuleMap.put("onlineStatus", QueryRuleEnum.LIKE_WITH_OR);
        QueryWrapper<FtuF411Device> queryWrapper = QueryGenerator.initQueryWrapper(ftuF411Device, req.getParameterMap(), customeRuleMap);
        Page<FtuF411Device> page = new Page<FtuF411Device>(pageNo, pageSize);
        IPage<FtuF411Device> pageList = ftuF411DeviceService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "统计查询")
    @GetMapping("/stat")
    @RequiresPermissions("ftu:ftu_f411_device:stat")
    public Result<List<Map<String, Object>>> stat() {
        QueryWrapper<FtuF411Device> queryWrapper = Wrappers.query();
        queryWrapper.select("count(1) as COUNT", "COMMUNICATION_MODE")
                .groupBy("COMMUNICATION_MODE");
        return Result.ok(ftuF411DeviceService.listMaps(queryWrapper));
    }


    /**
     * 添加
     *
     * @param ftuF411DevicePage
     * @return
     */
    @AutoLog(value = "通信终端-添加")
    @Operation(summary = "通信终端-添加")
    @RequiresPermissions("ftu:ftu_f411_device:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody FtuF411DevicePage ftuF411DevicePage) {
        FtuF411Device ftuF411Device = new FtuF411Device();
        BeanUtils.copyProperties(ftuF411DevicePage, ftuF411Device);
        ftuF411DeviceService.saveMain(ftuF411Device, ftuF411DevicePage.getFtuWarnInfoList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param ftuF411DevicePage
     * @return
     */
    @AutoLog(value = "通信终端-编辑")
    @Operation(summary = "通信终端-编辑")
    @RequiresPermissions("ftu:ftu_f411_device:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody FtuF411DevicePage ftuF411DevicePage) {
        FtuF411Device ftuF411Device = new FtuF411Device();
        BeanUtils.copyProperties(ftuF411DevicePage, ftuF411Device);
        FtuF411Device ftuF411DeviceEntity = ftuF411DeviceService.getById(ftuF411Device.getId());
        if (ftuF411DeviceEntity == null) {
            return Result.error("未找到对应数据");
        }
        ftuF411DeviceService.updateMain(ftuF411Device, ftuF411DevicePage.getFtuWarnInfoList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "通信终端-通过id删除")
    @Operation(summary = "通信终端-通过id删除")
    @RequiresPermissions("ftu:ftu_f411_device:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        ftuF411DeviceService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "通信终端-批量删除")
    @Operation(summary = "通信终端-批量删除")
    @RequiresPermissions("ftu:ftu_f411_device:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.ftuF411DeviceService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "通信终端-通过id查询")
    @Operation(summary = "通信终端-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<FtuF411Device> queryById(@RequestParam(name = "id", required = true) String id) {
        FtuF411Device ftuF411Device = ftuF411DeviceService.getById(id);
        if (ftuF411Device == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(ftuF411Device);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "告警信息通过主表ID查询")
    @Operation(summary = "告警信息主表ID查询")
    @GetMapping(value = "/queryFtuWarnInfoByMainId")
    public Result<List<FtuWarnInfo>> queryFtuWarnInfoListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<FtuWarnInfo> ftuWarnInfoList = ftuWarnInfoService.selectByMainId(id);
        return Result.OK(ftuWarnInfoList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param ftuF411Device
     */
    @RequiresPermissions("ftu:ftu_f411_device:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FtuF411Device ftuF411Device) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<FtuF411Device> queryWrapper = QueryGenerator.initQueryWrapper(ftuF411Device, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //配置选中数据查询条件
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        //Step.2 获取导出数据
        List<FtuF411Device> ftuF411DeviceList = ftuF411DeviceService.list(queryWrapper);

        // Step.3 组装pageList
        List<FtuF411DevicePage> pageList = new ArrayList<FtuF411DevicePage>();
        for (FtuF411Device main : ftuF411DeviceList) {
            FtuF411DevicePage vo = new FtuF411DevicePage();
            BeanUtils.copyProperties(main, vo);
            List<FtuWarnInfo> ftuWarnInfoList = ftuWarnInfoService.selectByMainId(main.getId());
            vo.setFtuWarnInfoList(ftuWarnInfoList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "通信终端列表");
        mv.addObject(NormalExcelConstants.CLASS, FtuF411DevicePage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("通信终端数据", "导出人:" + sysUser.getRealname(), "通信终端"));
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
    @RequiresPermissions("ftu:ftu_f411_device:importExcel")
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
                List<FtuF411DevicePage> list = ExcelImportUtil.importExcel(file.getInputStream(), FtuF411DevicePage.class, params);
                for (FtuF411DevicePage page : list) {
                    FtuF411Device po = new FtuF411Device();
                    BeanUtils.copyProperties(page, po);
                    ftuF411DeviceService.saveMain(po, page.getFtuWarnInfoList());
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
