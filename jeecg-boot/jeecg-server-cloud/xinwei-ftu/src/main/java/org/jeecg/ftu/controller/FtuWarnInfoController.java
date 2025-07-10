package org.jeecg.ftu.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.ftu.entity.FtuWarnInfo;
import org.jeecg.ftu.service.AutoLogService;
import org.jeecg.ftu.service.IFtuWarnInfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 告警记录
 * @Author: jeecg-boot
 * @Date:   2025-06-30
 * @Version: V1.0
 */
@Tag(name="告警记录")
@RestController
@RequestMapping("/ftu/ftuWarnInfo")
@Slf4j
public class FtuWarnInfoController extends JeecgController<FtuWarnInfo, IFtuWarnInfoService> {
	@Autowired
	private IFtuWarnInfoService ftuWarnInfoService;
	@Autowired
	private AutoLogService autoLogService;

	/**
	 * 分页列表查询
	 *
	 * @param ftuWarnInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "告警记录-分页列表查询")
	@Operation(summary="告警记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FtuWarnInfo>> queryPageList(FtuWarnInfo ftuWarnInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<FtuWarnInfo> queryWrapper = QueryGenerator.initQueryWrapper(ftuWarnInfo, req.getParameterMap());
		Page<FtuWarnInfo> page = new Page<FtuWarnInfo>(pageNo, pageSize);
		IPage<FtuWarnInfo> pageList = ftuWarnInfoService.page(page, queryWrapper);
		autoLogService.addLog("告警记录-分页列表查询", CommonConstant.LOG_TYPE_2,CommonConstant.OPERATE_TYPE_1,null);
		return Result.OK(pageList);
	}

	@Operation(summary = "根据设备查询事件统计")
	@GetMapping(value = "/getStatById")
	@RequiresPermissions("ftu:ftu_warn_info:eventStat")
	public Result<Map<String, Object>> getStatById(@RequestParam(name = "ftuId") String ftuId) {
		return Result.ok(ftuWarnInfoService.getStatById(ftuId));
	}

	@Operation(summary = "根据ftuId查询日范围事件")
	@RequiresPermissions("ftu:ftu_warn_info:getEventById")
	@GetMapping(value = "/getEventById")
	public Result<IPage<FtuWarnInfo>> getEventById(@RequestParam(name = "ftuId") String ftuId,
													@RequestParam(name = "Date") String date,
													@RequestParam(name = "event") String event,
													@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		return Result.ok(ftuWarnInfoService.getEventById(ftuId, date,event,pageNo,pageSize));
	}

	/**
	 *   添加
	 *
	 * @param ftuWarnInfo
	 * @return
	 */
	@AutoLog(value = "告警记录-添加")
	@Operation(summary="告警记录-添加")
	@RequiresPermissions("ftu:ftu_warn_info:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FtuWarnInfo ftuWarnInfo) {
		ftuWarnInfoService.save(ftuWarnInfo);

		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param ftuWarnInfo
	 * @return
	 */
	@AutoLog(value = "告警记录-编辑")
	@Operation(summary="告警记录-编辑")
	@RequiresPermissions("ftu:ftu_warn_info:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FtuWarnInfo ftuWarnInfo) {
		ftuWarnInfoService.updateById(ftuWarnInfo);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "告警记录-通过id删除")
	@Operation(summary="告警记录-通过id删除")
	@RequiresPermissions("ftu:ftu_warn_info:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ftuWarnInfoService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "告警记录-批量删除")
	@Operation(summary="告警记录-批量删除")
	@RequiresPermissions("ftu:ftu_warn_info:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ftuWarnInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "告警记录-通过id查询")
	@Operation(summary="告警记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FtuWarnInfo> queryById(@RequestParam(name="id",required=true) String id) {
		FtuWarnInfo ftuWarnInfo = ftuWarnInfoService.getById(id);
		if(ftuWarnInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ftuWarnInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ftuWarnInfo
    */
    @RequiresPermissions("ftu:ftu_warn_info:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FtuWarnInfo ftuWarnInfo) {
        return super.exportXls(request, ftuWarnInfo, FtuWarnInfo.class, "告警记录");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("ftu:ftu_warn_info:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, FtuWarnInfo.class);
    }

}
