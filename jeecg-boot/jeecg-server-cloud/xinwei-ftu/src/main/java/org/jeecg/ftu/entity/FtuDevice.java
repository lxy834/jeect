package org.jeecg.ftu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: FTU终端
 * @Author: jeecg-boot
 * @Date: 2025-06-23
 * @Version: V1.0
 */
@Schema(description = "FTU终端")
@Data
@TableName("ftu_device")
public class FtuDevice implements Serializable {
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
     * CPU状态
     */
    @Excel(name = "CPU状态", width = 15, dicCode = "cpu_status")
    @Dict(dicCode = "cpu_status")
    @Schema(description = "CPU状态")
    private java.lang.Integer cpuStatus;
    /**
     * 电源状态
     */
    @Excel(name = "电源状态", width = 15, dicCode = "ps_status")
    @Dict(dicCode = "ps_status")
    @Schema(description = "电源状态")
    private java.lang.Integer psStatus;
    /**
     * 点位名称
     */
    @Excel(name = "点位名称", width = 15)
    @Schema(description = "点位名称")
    private java.lang.String deviceName;
    /**
     * 线路名称
     */
    @Excel(name = "线路名称", width = 15)
    @Schema(description = "线路名称")
    private java.lang.String insLineName;
    /**
     * 开关状态
     */
    @Excel(name = "开关状态", width = 15, dicCode = "switch_status")
    @Dict(dicCode = "switch_status")
    @Schema(description = "开关状态")
    private java.lang.Integer switchStatus;

    private java.lang.Integer status;

    /**
     * 故障指示
     */
    @Excel(name = "故障指示", width = 15)
    @Schema(description = "故障指示")
    private java.lang.String fault;
    /**
     * 对时时间
     */
    @Excel(name = "对时时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "对时时间")
    private java.util.Date timingTime;
    /**
     * IP地址
     */
    @Excel(name = "IP地址", width = 15)
    @Schema(description = "IP地址")
    private java.lang.String ip;
    /**
     * 责任区
     */
    @Excel(name = "责任区", width = 15)
    @Schema(description = "责任区")
    private java.lang.String areaOfRespone;
    /**
     * 厂家
     */
    @Excel(name = "厂家", width = 15)
    @Schema(description = "厂家")
    private java.lang.String factory;
    /**
     * 投运时间
     */
    @Excel(name = "投运时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "投运时间")
    private java.util.Date commTime;
    /**
     * 型号
     */
    @Excel(name = "型号", width = 15)
    @Schema(description = "型号")
    private java.lang.String ftuModel;
    /**
     * 变电站
     */
    @Excel(name = "变电站", width = 15)
    @Schema(description = "变电站")
    private java.lang.String station;

    @Schema(description = "纬度")
    private java.lang.Double lat;

    @Schema(description = "经度")
    private java.lang.Double lng;

    private Double ratedCurrent;


}
