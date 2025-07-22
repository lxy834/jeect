package org.jeecg.ftu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 告警记录
 * @Author: jeecg-boot
 * @Date: 2025-06-30
 * @Version: V1.0
 */
@Data
@TableName("ftu_warn_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "告警记录")
public class FtuWarnInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 告警信息
     */
    @Excel(name = "告警信息", width = 15)
    @Schema(description = "告警信息")
    private java.lang.String warnInfo;
    /**
     * 告警类别
     */
    @Excel(name = "展示类别", width = 15, dicCode = "device_type")
    @Dict(dicCode = "device_type")
    @Schema(description = "展示类别")
    private java.lang.String deviceType;
    /**
     * 设备id
     */
    @Excel(name = "设备id", width = 15)
    @Schema(description = "设备id")
    private java.lang.String deviceId;
    /**
     * 告警时间
     */
    @Excel(name = "告警时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "告警时间")
    private java.util.Date warnTime;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称", width = 15)
    @Schema(description = "设备名称")
    private java.lang.String deviceName;
    /**
     * 点位
     */
    @Excel(name = "点位", width = 15)
    @Schema(description = "点位")
    private java.lang.String insLocation;
    /**
     * 线路位置
     */
    @Excel(name = "线路位置", width = 15)
    @Schema(description = "线路位置")
    private java.lang.String lineLocation;

    @Schema(description = "统计类型")
    private String warnType;

    private String tenantId;

}
