package org.jeecg.modules.system.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysServerInfo;
import org.jeecg.modules.system.service.ISysServerInfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 系统台账
 * @Author: jeecg-boot
 * @Date:   2025-07-10
 * @Version: V1.0
 */
@Tag(name="系统台账")
@RestController
@RequestMapping("/sys/sysServerInfo")
@Slf4j
public class SysServerInfoController extends JeecgController<SysServerInfo, ISysServerInfoService> {
	@Autowired
	private ISysServerInfoService sysServerInfoService;

	/**
	 * 分页列表查询
	 *
	 * @param sysServerInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "系统台账-分页列表查询")
	@Operation(summary="系统台账-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysServerInfo>> queryPageList(SysServerInfo sysServerInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<SysServerInfo> queryWrapper = QueryGenerator.initQueryWrapper(sysServerInfo, req.getParameterMap());
		Page<SysServerInfo> page = new Page<SysServerInfo>(pageNo, pageSize);
		IPage<SysServerInfo> pageList = sysServerInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param sysServerInfo
	 * @return
	 */
	@AutoLog(value = "系统台账-添加")
	@Operation(summary="系统台账-添加")
	@RequiresPermissions("system:sys_server_info:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SysServerInfo sysServerInfo) {
		sysServerInfoService.save(sysServerInfo);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param sysServerInfo
	 * @return
	 */
	@AutoLog(value = "系统台账-编辑")
	@Operation(summary="系统台账-编辑")
	@RequiresPermissions("system:sys_server_info:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SysServerInfo sysServerInfo) {
		sysServerInfoService.updateById(sysServerInfo);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "系统台账-通过id删除")
	@Operation(summary="系统台账-通过id删除")
	@RequiresPermissions("system:sys_server_info:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		sysServerInfoService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "系统台账-批量删除")
	@Operation(summary="系统台账-批量删除")
	@RequiresPermissions("system:sys_server_info:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysServerInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "系统台账-通过id查询")
	@Operation(summary="系统台账-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysServerInfo> queryById(@RequestParam(name="id",required=true) String id) {
		SysServerInfo sysServerInfo = sysServerInfoService.getById(id);
		if(sysServerInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sysServerInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysServerInfo
    */
    @RequiresPermissions("system:sys_server_info:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysServerInfo sysServerInfo) {
        return super.exportXls(request, sysServerInfo, SysServerInfo.class, "系统台账");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("system:sys_server_info:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysServerInfo.class);
    }

}
