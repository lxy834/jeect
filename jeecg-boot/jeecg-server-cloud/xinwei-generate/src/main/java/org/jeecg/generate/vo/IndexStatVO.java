package org.jeecg.generate.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class IndexStatVO implements Serializable {

    private Integer totalPower;

    private Double mileage;

    private Integer yearlyTasks;

    private Integer maintenanceCount;

    private Integer totalFuel;

    private Double fuelPerKwh;

    private Integer totalRevenue;

    private Integer countHours;


}
