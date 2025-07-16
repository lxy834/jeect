package org.jeecg.ftu.controller;

import cn.hutool.core.util.HexUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class Test {

    public static void main(String[] args) {
        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        JSONArray devicesArray = new JSONArray();
        com.alibaba.fastjson.JSONObject device = new com.alibaba.fastjson.JSONObject();
        device.put("deviceId", "FTU_DeviceId");
        JSONArray servicesArray = new JSONArray();
        com.alibaba.fastjson.JSONObject service = new com.alibaba.fastjson.JSONObject();
        service.put("serviceId", "running");
        service.put("eventTime", "202507081050");
        com.alibaba.fastjson.JSONObject data = new JSONObject();
        data.put("time", "2025-07-08 10:10:10");
        data.put("uabFor4",101); //4#母线Uab电压
        data.put("ucbFor4",101); //4#母线Ucb电压
        data.put("currentFor201InputA",5.5);//201进线A相电流
        data.put("currentFor211OutputB",5.5);//211出线B相电流
        data.put("activeFor201Input",5.5);//201进线有功功率
        data.put("factorFor201Output",5.5);//201进线功率因数
        service.put("data", data);
        servicesArray.add(service);
        device.put("services", servicesArray);
        devicesArray.add(device);
        json.put("devices", devicesArray);

        System.out.println(json);
    }

}
