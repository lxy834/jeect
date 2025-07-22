package org.jeecg.generate.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jeecg.generate.entity.FdqOrderStep;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 工单台账
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Data
@Schema(description = "工单台账")
public class FdqOrderPage {

    /**
     * 主键
     */
    @Schema(description = "主键")
    private java.lang.String id;
    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private java.util.Date updateTime;
    /**
     * 所属部门
     */
    @Schema(description = "所属部门")
    private java.lang.String sysOrgCode;
    /**
     * 资产车牌
     */
    @Excel(name = "资产车牌", width = 15)
    @Schema(description = "资产车牌")
    private java.lang.String plateNumber;
    /**
     * 工单类型
     */
    @Excel(name = "工单类型", width = 15)
    @Schema(description = "工单类型")
    private java.lang.String orderType;
    /**
     * 目标用户
     */
    @Excel(name = "目标用户", width = 15)
    @Schema(description = "目标用户")
    private java.lang.String targetUser;
    /**
     * 市局
     */
    @Excel(name = "市局", width = 15)
    @Schema(description = "市局")
    private java.lang.String deptName;
    /**
     * 地区局
     */
    @Excel(name = "地区局", width = 15)
    @Schema(description = "地区局")
    private java.lang.String bureau;
    /**
     * 工单状态
     */
    @Excel(name = "工单状态", width = 15)
    @Schema(description = "工单状态")
    private java.lang.String orderStatus;
    /**
     * 发电时间
     */
    @Excel(name = "发电时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "发电时间")
    private java.util.Date beginTime;
    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间")
    private java.util.Date endTime;
    /**
     * 发电量
     */
    @Excel(name = "发电量", width = 15)
    @Schema(description = "发电量")
    private java.lang.Double stat;
    /**
     * 油耗
     */
    @Excel(name = "油耗", width = 15)
    @Schema(description = "油耗")
    private java.lang.Double fuel;
    /**
     * 行驶里程
     */
    @Excel(name = "行驶里程", width = 15)
    @Schema(description = "行驶里程")
    private java.lang.Double mileage;

    @ExcelCollection(name = "任务详情")
    @Schema(description = "任务详情")
    private List<FdqOrderStep> fdqOrderStepList;

}
