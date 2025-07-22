package org.jeecg.generate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 历史数据
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Schema(description = "历史数据")
@Data
@TableName("fdq_controller")
public class FdqController implements Serializable {
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
     * A项电压
     */
    @Excel(name = "A项电压", width = 15)
    @Schema(description = "A项电压")
    private java.lang.Double voltageA;
    /**
     * B项电压
     */
    @Excel(name = "B项电压", width = 15)
    @Schema(description = "B项电压")
    private java.lang.Double voltageB;
    /**
     * C项电压
     */
    @Excel(name = "C项电压", width = 15)
    @Schema(description = "C项电压")
    private java.lang.Double voltageC;
    /**
     * 线压A
     */
    @Excel(name = "线压A", width = 15)
    @Schema(description = "线压A")
    private java.lang.Double lineVoltageA;
    /**
     * 线压B
     */
    @Excel(name = "线压B", width = 15)
    @Schema(description = "线压B")
    private java.lang.Double lineVoltageB;
    /**
     * 线压C
     */
    @Excel(name = "线压C", width = 15)
    @Schema(description = "线压C")
    private java.lang.Double lineVoltageC;
    /**
     * 电流A
     */
    @Excel(name = "电流A", width = 15)
    @Schema(description = "电流A")
    private java.lang.Double currentA;
    /**
     * 电流B
     */
    @Excel(name = "电流B", width = 15)
    @Schema(description = "电流B")
    private java.lang.Double currentB;
    /**
     * 电流C
     */
    @Excel(name = "电流C", width = 15)
    @Schema(description = "电流C")
    private java.lang.Double currentC;
    /**
     * 转速
     */
    @Excel(name = "转速", width = 15)
    @Schema(description = "转速")
    private java.lang.Integer rpm;
    /**
     * 发电频率
     */
    @Excel(name = "发电频率", width = 15)
    @Schema(description = "发电频率")
    private java.lang.Double freqHz;
    /**
     * 有功A
     */
    @Excel(name = "有功A", width = 15)
    @Schema(description = "有功A")
    private java.lang.Double activeA;
    /**
     * 有功B
     */
    @Excel(name = "有功B", width = 15)
    @Schema(description = "有功B")
    private java.lang.Double activeB;
    /**
     * 有功C
     */
    @Excel(name = "有功C", width = 15)
    @Schema(description = "有功C")
    private java.lang.Double activeC;
    /**
     * 功率因素A
     */
    @Excel(name = "功率因素A", width = 15)
    @Schema(description = "功率因素A")
    private java.lang.Double factorA;
    /**
     * 功率因素B
     */
    @Excel(name = "功率因素B", width = 15)
    @Schema(description = "功率因素B")
    private java.lang.Double factorB;
    /**
     * 功率因素C
     */
    @Excel(name = "功率因素C", width = 15)
    @Schema(description = "功率因素C")
    private java.lang.Double factorC;
    /**
     * 电池电压
     */
    @Excel(name = "电池电压", width = 15)
    @Schema(description = "电池电压")
    private java.lang.Double battery;
    /**
     * 水温
     */
    @Excel(name = "水温", width = 15)
    @Schema(description = "水温")
    private java.lang.Double waterTemperature;
    /**
     * 发动机状态
     */
    @Excel(name = "发动机状态", width = 15)
    @Schema(description = "发动机状态")
    private java.lang.Integer status;
    /**
     * 运行小时
     */
    @Excel(name = "运行小时", width = 15)
    @Schema(description = "运行小时")
    private java.lang.Integer runningHours;
    /**
     * 下次维护剩余时间
     */
    @Excel(name = "下次维护剩余时间", width = 15)
    @Schema(description = "下次维护剩余时间")
    private java.lang.Integer nextRepair;
    /**
     * 启动次数
     */
    @Excel(name = "启动次数", width = 15)
    @Schema(description = "启动次数")
    private java.lang.Integer numberOfLaunches;
    /**
     * 总发电量
     */
    @Excel(name = "总发电量", width = 15)
    @Schema(description = "总发电量")
    private java.lang.Double kwh;
    /**
     * 无功
     */
    @Excel(name = "无功", width = 15)
    @Schema(description = "无功")
    private java.lang.Double kwarh;
    /**
     * 车牌号
     */
    @Schema(description = "车牌号")
    private java.lang.String plateNumber;
    /**
     * 自定义属性
     */
    @Excel(name = "自定义属性", width = 15)
    @Schema(description = "自定义属性")
    private java.lang.String customization;
    
}
