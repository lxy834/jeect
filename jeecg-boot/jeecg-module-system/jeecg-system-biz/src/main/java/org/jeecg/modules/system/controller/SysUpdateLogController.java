package org.jeecg.modules.system.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.system.entity.SysUpdateLog;
import org.jeecg.modules.system.service.ISysUpdateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 系统更新日志
 * @Author: jeecg-boot
 * @Date:   2025-07-04
 * @Version: V1.0
 */
@Tag(name="系统更新日志")
@RestController
@RequestMapping("/sys/update/log")
@Slf4j
public class SysUpdateLogController extends JeecgController<SysUpdateLog, ISysUpdateLogService> {
	@Autowired
	private ISysUpdateLogService sysUpdateLogService;

	/**
	 * 分页列表查询
	 *
	 * @param sysUpdateLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "系统更新日志-分页列表查询")
	@Operation(summary="系统更新日志-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysUpdateLog>> queryPageList(SysUpdateLog sysUpdateLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<SysUpdateLog> queryWrapper = QueryGenerator.initQueryWrapper(sysUpdateLog, req.getParameterMap());
		Page<SysUpdateLog> page = new Page<SysUpdateLog>(pageNo, pageSize);
		IPage<SysUpdateLog> pageList = sysUpdateLogService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param sysUpdateLog
	 * @return
	 */
	@AutoLog(value = "系统更新日志-添加")
	@Operation(summary="系统更新日志-添加")
	@RequiresPermissions("sys:sys_update_log:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SysUpdateLog sysUpdateLog) {
		sysUpdateLogService.save(sysUpdateLog);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param sysUpdateLog
	 * @return
	 */
	@AutoLog(value = "系统更新日志-编辑")
	@Operation(summary="系统更新日志-编辑")
	@RequiresPermissions("sys:sys_update_log:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SysUpdateLog sysUpdateLog) {
		sysUpdateLogService.updateById(sysUpdateLog);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "系统更新日志-通过id删除")
	@Operation(summary="系统更新日志-通过id删除")
	@RequiresPermissions("sys:sys_update_log:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		sysUpdateLogService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "系统更新日志-批量删除")
	@Operation(summary="系统更新日志-批量删除")
	@RequiresPermissions("sys:sys_update_log:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysUpdateLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "系统更新日志-通过id查询")
	@Operation(summary="系统更新日志-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysUpdateLog> queryById(@RequestParam(name="id",required=true) String id) {
		SysUpdateLog sysUpdateLog = sysUpdateLogService.getById(id);
		if(sysUpdateLog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sysUpdateLog);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysUpdateLog
    */
    @RequiresPermissions("sys:sys_update_log:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysUpdateLog sysUpdateLog) {
        return super.exportXls(request, sysUpdateLog, SysUpdateLog.class, "系统更新日志");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("sys:sys_update_log:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysUpdateLog.class);
    }

}
