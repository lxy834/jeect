package org.jeecg.ftu.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Description: 通信终端
 * @Author: jeecg-boot
 * @Date:   2025-06-23
 * @Version: V1.0
 */
@Schema(description="通信终端")
@Data
@TableName("ftu_f411_device")
public class FtuF411Device implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private java.lang.String id;
	/**创建人*/
    @Schema(description = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @Schema(description = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @Schema(description = "所属部门")
    private java.lang.String sysOrgCode;
	/**通信模式*/
	@Excel(name = "通信模式", width = 15)
    @Dict(dicCode = "comm_model")
    @Schema(description = "通信模式")
    private java.lang.Integer communicationMode;
	/**租户id*/
	@Excel(name = "租户id", width = 15)
    @Schema(description = "租户id")
    private java.lang.String tenantId;
	/**安装位置*/
	@Excel(name = "安装位置", width = 15)
    @Schema(description = "安装位置")
    private java.lang.String insLocation;
	/**在线状态*/
	@Excel(name = "在线状态", width = 15, dicCode = "online_status")
    @Dict(dicCode = "online_status")
    @Schema(description = "在线状态")
    private java.lang.Integer onlineStatus;
	/**北斗卡号*/
	@Excel(name = "北斗卡号", width = 15)
    @Schema(description = "北斗卡号")
    private java.lang.Integer bdCard;
	/**5G卡号*/
	@Excel(name = "5G卡号", width = 15)
    @Schema(description = "5G卡号")
    private java.lang.String apnCard;
	/**关联的FTU*/
	@Excel(name = "关联的FTU", width = 15)
    @Schema(description = "关联的FTU")
    private java.lang.String ftuId;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @Schema(description = "设备名称")
    private java.lang.String deviceName;
}
