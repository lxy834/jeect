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
import java.util.Date;

/**
 * @Description: 定位数据
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Data
@TableName("fdq_track")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "定位数据")
public class FdqTrack implements Serializable {
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
     * 经度
     */
    @Excel(name = "经度", width = 15)
    @Schema(description = "经度")
    private java.lang.Double lng;
    /**
     * 纬度
     */
    @Excel(name = "纬度", width = 15)
    @Schema(description = "纬度")
    private java.lang.Double lat;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @Schema(description = "状态")
    private java.lang.String motionStatus;
    /**
     * 速度
     */
    @Excel(name = "速度", width = 15)
    @Schema(description = "速度")
    private java.lang.String speed;
    /**
     * 车牌号
     */
    @Excel(name = "车牌号", width = 15)
    @Schema(description = "车牌号")
    private java.lang.String plateNumber;

    private Date satelliteTime;

    private Double angle;
}
