package org.jeecg.generate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class FdqTrackVO {

    @Schema(description = "资产编号（车牌号）")
    private String plateNumber;
    @Schema(description = "状态 POWER_BASIC表power 0：移动  1：工作  2：驻留  3：离线")
    private Integer power;
    @Schema(description = "上传时间")
    private Date createTime;
    @Schema(description = "上传时间(时间戳)")
    private Long timestamp;
    @Schema(description = "经度")
    private Double lng;
    @Schema(description = "纬度")
    private Double lat;
    @Schema(description = "速度（m/s）")
    private Double speed;


//    public FdqTrackVO setCreateTime(Date time) {
//        this.createTime = time;
//        this.timestamp = time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
//        return this;
//    }

}
