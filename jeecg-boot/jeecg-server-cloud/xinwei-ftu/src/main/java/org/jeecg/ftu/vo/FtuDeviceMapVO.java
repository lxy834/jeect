package org.jeecg.ftu.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FtuDeviceMapVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double lng;

    private Double lat;

    private String deviceName;

    private String id;

    private String insLineName;

    private Integer status;

    private Integer communicationMode;

    private String ip;

    private String station;

    private Integer onlineStatus;
    
}
