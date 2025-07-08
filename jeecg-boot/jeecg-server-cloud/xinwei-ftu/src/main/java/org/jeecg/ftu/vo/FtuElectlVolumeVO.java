package org.jeecg.ftu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class FtuElectlVolumeVO {

    /**有功*/
    @Excel(name = "有功", width = 15)
    @Schema(description = "有功")
    private java.lang.Double activePower;
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

    private String insLineName;

}
