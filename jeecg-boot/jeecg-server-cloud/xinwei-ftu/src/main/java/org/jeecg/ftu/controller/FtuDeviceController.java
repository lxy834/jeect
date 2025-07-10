package org.jeecg.ftu.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.util.RestUtil;
import org.jeecg.ftu.vo.FtuElectlVolumeVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.ftu.entity.FtuF411Device;
import org.jeecg.ftu.entity.FtuElectlVolume;
import org.jeecg.ftu.entity.FtuWarnInfo;
import org.jeecg.ftu.entity.FtuDevice;
import org.jeecg.ftu.vo.FtuDevicePage;
import org.jeecg.ftu.service.IFtuDeviceService;
import org.jeecg.ftu.service.IFtuF411DeviceService;
import org.jeecg.ftu.service.IFtuElectlVolumeService;
import org.jeecg.ftu.service.IFtuWarnInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;


 /**
 * @Description: FTU终端
 * @Author: jeecg-boot
 * @Date:   2025-06-23
 * @Version: V1.0
 */
@Tag(name="FTU终端")
@RestController
@RequestMapping("/ftu/ftuDevice")
@Slf4j
public class FtuDeviceController {
	@Autowired
	private IFtuDeviceService ftuDeviceService;
	@Autowired
	private IFtuF411DeviceService ftuF411DeviceService;
	@Autowired
	private IFtuElectlVolumeService ftuElectlVolumeService;
	@Autowired
	private IFtuWarnInfoService ftuWarnInfoService;

	/**
	 * 分页列表查询
	 *
	 * @param ftuDevice
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "FTU终端-分页列表查询")
	@Operation(summary="FTU终端-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FtuDevice>> queryPageList(FtuDevice ftuDevice,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        // 自定义查询规则
        Map<String, QueryRuleEnum> customeRuleMap = new HashMap<>();
        // 自定义多选的查询规则为：LIKE_WITH_OR
        customeRuleMap.put("cpuStatus", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("psStatus", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("switchStatus", QueryRuleEnum.LIKE_WITH_OR);
        QueryWrapper<FtuDevice> queryWrapper = QueryGenerator.initQueryWrapper(ftuDevice, req.getParameterMap(),customeRuleMap);
		Page<FtuDevice> page = new Page<FtuDevice>(pageNo, pageSize);
		IPage<FtuDevice> pageList = ftuDeviceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param ftuDevicePage
	 * @return
	 */
	@AutoLog(value = "FTU终端-添加")
	@Operation(summary="FTU终端-添加")
    @RequiresPermissions("ftu:ftu_device:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FtuDevicePage ftuDevicePage) {
		FtuDevice ftuDevice = new FtuDevice();
		BeanUtils.copyProperties(ftuDevicePage, ftuDevice);
		ftuDeviceService.saveMain(ftuDevice, ftuDevicePage.getFtuF411DeviceList(),ftuDevicePage.getFtuElectlVolumeList(),ftuDevicePage.getFtuWarnInfoList());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param ftuDevicePage
	 * @return
	 */
	@AutoLog(value = "FTU终端-编辑")
	@Operation(summary="FTU终端-编辑")
    @RequiresPermissions("ftu:ftu_device:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FtuDevicePage ftuDevicePage) {
		FtuDevice ftuDevice = new FtuDevice();
		BeanUtils.copyProperties(ftuDevicePage, ftuDevice);
		FtuDevice ftuDeviceEntity = ftuDeviceService.getById(ftuDevice.getId());
		if(ftuDeviceEntity==null) {
			return Result.error("未找到对应数据");
		}
		ftuDeviceService.updateMain(ftuDevice, ftuDevicePage.getFtuF411DeviceList(),ftuDevicePage.getFtuElectlVolumeList(),ftuDevicePage.getFtuWarnInfoList());
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "FTU终端-通过id删除")
	@Operation(summary="FTU终端-通过id删除")
    @RequiresPermissions("ftu:ftu_device:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ftuDeviceService.delMain(id);
		return Result.OK("删除成功!");
	}

	@Operation(summary = "电气量数据")
	@GetMapping("/volume")
	@RequiresPermissions("ftu:ftu_device:volume")
	public Result<List<FtuElectlVolumeVO>> queryFtuElectlVolumeList() {
		return Result.ok(ftuDeviceService.queryFtuElectlVolumeList());
	}

	 @Operation(summary = "电气量数据")
	 @GetMapping("/getVolumeById")
	 @RequiresPermissions("ftu:ftu_device:getVolumeById")
	 public Result<Map<String,Object>> getVolumeStatByFtuId(@RequestParam(name="ftuId",required=false) String ftuId) {
		 return Result.ok(ftuDeviceService.getVolumeStatByFtuId(ftuId));
	 }

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "FTU终端-批量删除")
	@Operation(summary="FTU终端-批量删除")
    @RequiresPermissions("ftu:ftu_device:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ftuDeviceService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "FTU终端-通过id查询")
	@Operation(summary="FTU终端-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FtuDevice> queryById(@RequestParam(name="id",required=true) String id) {
		FtuDevice ftuDevice = ftuDeviceService.getById(id);
		if(ftuDevice==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ftuDevice);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "通信终端通过主表ID查询")
	@Operation(summary="通信终端主表ID查询")
	@GetMapping(value = "/queryFtuF411DeviceByMainId")
	public Result<List<FtuF411Device>> queryFtuF411DeviceListByMainId(@RequestParam(name="id",required=true) String id) {
		List<FtuF411Device> ftuF411DeviceList = ftuF411DeviceService.selectByMainId(id);
		return Result.OK(ftuF411DeviceList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "电气量信息通过主表ID查询")
	@Operation(summary="电气量信息主表ID查询")
	@GetMapping(value = "/queryFtuElectlVolumeByMainId")
	public Result<List<FtuElectlVolume>> queryFtuElectlVolumeListByMainId(@RequestParam(name="id",required=true) String id) {
		List<FtuElectlVolume> ftuElectlVolumeList = ftuElectlVolumeService.selectByMainId(id);
		return Result.OK(ftuElectlVolumeList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "告警信息通过主表ID查询")
	@Operation(summary="告警信息主表ID查询")
	@GetMapping(value = "/queryFtuWarnInfoByMainId")
	public Result<List<FtuWarnInfo>> queryFtuWarnInfoListByMainId(@RequestParam(name="id",required=true) String id) {
		List<FtuWarnInfo> ftuWarnInfoList = ftuWarnInfoService.selectByMainId(id);
		return Result.OK(ftuWarnInfoList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ftuDevice
    */
    @RequiresPermissions("ftu:ftu_device:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FtuDevice ftuDevice) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<FtuDevice> queryWrapper = QueryGenerator.initQueryWrapper(ftuDevice, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<FtuDevice> ftuDeviceList = ftuDeviceService.list(queryWrapper);

      // Step.3 组装pageList
      List<FtuDevicePage> pageList = new ArrayList<FtuDevicePage>();
      for (FtuDevice main : ftuDeviceList) {
          FtuDevicePage vo = new FtuDevicePage();
          BeanUtils.copyProperties(main, vo);
          List<FtuF411Device> ftuF411DeviceList = ftuF411DeviceService.selectByMainId(main.getId());
          vo.setFtuF411DeviceList(ftuF411DeviceList);
          List<FtuElectlVolume> ftuElectlVolumeList = ftuElectlVolumeService.selectByMainId(main.getId());
          vo.setFtuElectlVolumeList(ftuElectlVolumeList);
          List<FtuWarnInfo> ftuWarnInfoList = ftuWarnInfoService.selectByMainId(main.getId());
          vo.setFtuWarnInfoList(ftuWarnInfoList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "FTU终端列表");
      mv.addObject(NormalExcelConstants.CLASS, FtuDevicePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("FTU终端数据", "导出人:"+sysUser.getRealname(), "FTU终端"));
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
    @RequiresPermissions("ftu:ftu_device:importExcel")
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
              List<FtuDevicePage> list = ExcelImportUtil.importExcel(file.getInputStream(), FtuDevicePage.class, params);
              for (FtuDevicePage page : list) {
                  FtuDevice po = new FtuDevice();
                  BeanUtils.copyProperties(page, po);
                  ftuDeviceService.saveMain(po, page.getFtuF411DeviceList(),page.getFtuElectlVolumeList(),page.getFtuWarnInfoList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
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
