package org.jeecg.ftu.controller;

import lombok.Data;

@Data
public class Demo {

    private Double voltageA;
    private Double voltageB;
    private Double voltageC;


    private Double lineVoltageA;
    private Double lineVoltageB;
    private Double lineVoltageC;

    private Double currentA;
    private Double currentB;
    private Double currentC;

    private Double activeA;
    private Double activeB;
    private Double activeC;

    private Double rpm;

    private Double freqHz;

    private Double factorA;
    private Double factorB;
    private Double factorC;

    private Double battery;

    private Double waterTemperature;

    private Integer status;

    private Double runningHours;

    private Double nextRepair;

    private Double numberOfLaunches;

    private Double kwh;

    private Double kwarh;

    private String customization;

}
