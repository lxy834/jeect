package org.jeecg.ftu.entity;

import java.io.Serializable;
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
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 电气量信息
 * @Author: jeecg-boot
 * @Date:   2025-06-23
 * @Version: V1.0
 */
@Schema(description="电气量信息")
@Data
@TableName("ftu_electl_volume")
public class FtuElectlVolume implements Serializable {
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
	/**有功*/
	@Excel(name = "有功", width = 15)
    @Schema(description = "有功")
    private java.lang.Double activePower;
	/**无功*/
	@Excel(name = "无功", width = 15)
    @Schema(description = "无功")
    private java.lang.Double reactivePower;
	/**功率因数*/
	@Excel(name = "功率因数", width = 15)
    @Schema(description = "功率因数")
    private java.lang.Double factor;
	/**电压*/
	@Excel(name = "电压", width = 15)
    @Schema(description = "电压")
    private java.lang.Double voltage;
	/**电流*/
	@Excel(name = "电流", width = 15)
    @Schema(description = "电流")
    private java.lang.Double ftuCurrent;

    private String sendMode;

	/**关联的FTU*/
    @Schema(description = "关联的FTU")
    private java.lang.String ftuId;
}
