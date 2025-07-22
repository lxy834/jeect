package org.jeecg.generate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 系统
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Data
@TableName("fdq_heart_beat")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "系统")
public class FdqHeartBeat implements Serializable {
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
     * GPS时间
     */
    @Excel(name = "GPS时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "GPS时间")
    private java.lang.String gpsTime;
    /**
     * 运行时长
     */
    @Excel(name = "运行时长", width = 15)
    @Schema(description = "运行时长")
    private java.lang.Double runningTime;
    /**
     * 心跳频率
     */
    @Excel(name = "心跳频率", width = 15)
    @Schema(description = "心跳频率")
    private java.lang.Integer heartbeatInterval;
    /**
     * 开门状态
     */
    @Excel(name = "开门状态", width = 15)
    @Schema(description = "开门状态")
    private java.lang.Integer doorStatus;
    private Integer powerMode;
    /**
     * 外部电压
     */
    @Excel(name = "外部电压", width = 15)
    @Schema(description = "外部电压")
    private java.lang.Double externalVoltage;
    /**
     * 内部电压
     */
    @Excel(name = "内部电压", width = 15)
    @Schema(description = "内部电压")
    private java.lang.Double internalBattery;
    /**
     * 信号质量
     */
    @Excel(name = "信号质量", width = 15)
    @Schema(description = "信号质量")
    private java.lang.Double signalQuality;
    /**
     * 固件版本
     */
    @Excel(name = "固件版本", width = 15)
    @Schema(description = "固件版本")
    private java.lang.String appVersion;
    /**
     * 车牌号
     */
    @Excel(name = "车牌号", width = 15)
    @Schema(description = "车牌号")
    private java.lang.String plateNumber;
}
