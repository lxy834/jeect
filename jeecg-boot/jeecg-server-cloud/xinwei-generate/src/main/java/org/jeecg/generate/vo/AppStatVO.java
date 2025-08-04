package org.jeecg.generate.vo;

import lombok.Data;

/**
 * @author averice
 */
@Data
public class AppStatVO {

    private Integer carCount;

    private Integer lastCarCount;

    private Integer working;

    private Integer lastWorking;

    private Integer manual;

    private Integer lastManual;

    private Double volume;

    private Double lastVolume;

}
