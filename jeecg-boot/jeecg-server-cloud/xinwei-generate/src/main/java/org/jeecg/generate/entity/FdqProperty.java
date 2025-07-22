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
 * @Description: 应急装备
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
@Schema(description = "应急装备")
@Data
@TableName("fdq_property")
public class FdqProperty implements Serializable {
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
     * 部门
     */
    @Excel(name = "部门", width = 15)
    @Schema(description = "部门")
    private java.lang.String deptName;
    /**
     * 车牌号
     */
    @Excel(name = "车牌号", width = 15)
    @Schema(description = "车牌号")
    private java.lang.String plateNumber;
    /**
     * 型号
     */
    @Excel(name = "型号", width = 15)
    @Schema(description = "型号")
    private java.lang.String brand;
    /**
     * 资产主人
     */
    @Excel(name = "资产主人", width = 15)
    @Schema(description = "资产主人")
    private java.lang.String master;
    /**
     * 联系方式
     */
    @Excel(name = "联系方式", width = 15)
    @Schema(description = "联系方式")
    private java.lang.String phone;
    /**
     * 地区局
     */
    @Excel(name = "地区局", width = 15)
    @Schema(description = "地区局")
    private java.lang.String bureau;
    /**
     * 控制器型号
     */
    @Excel(name = "控制器型号", width = 15)
    @Schema(description = "控制器型号")
    private java.lang.String ctrlModel;
    /**
     * 修正因数
     */
    @Excel(name = "修正因数", width = 15)
    @Schema(description = "修正因数")
    private java.lang.Double fixesFactor;
    /**
     * 最后经度
     */
    @Excel(name = "最后经度", width = 15)
    @Schema(description = "最后经度")
    private java.lang.Double lastLng;
    /**
     * 最后纬度
     */
    @Excel(name = "最后纬度", width = 15)
    @Schema(description = "最后纬度")
    private java.lang.Double lastLat;

    private Integer lastStatus;

    private String sn;

}
