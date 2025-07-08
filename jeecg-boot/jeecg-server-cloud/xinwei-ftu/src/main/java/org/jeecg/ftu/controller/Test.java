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
        device.put("deviceId", "FDQ495060299771");
        JSONArray servicesArray = new JSONArray();
        com.alibaba.fastjson.JSONObject service = new com.alibaba.fastjson.JSONObject();
        service.put("serviceId", "Location");
        service.put("eventTime", "202507081050");
        com.alibaba.fastjson.JSONObject data = new JSONObject();
        data.put("time", "2025-07-08 10:10:10");
        data.put("lng",104.555555);
        data.put("lat",24.555555);
        data.put("speed",5.5);
        data.put("headingAngle",156);
        data.put("status",1);
        service.put("data", data);
        servicesArray.add(service);
        device.put("services", servicesArray);
        devicesArray.add(device);
        json.put("devices", devicesArray);

        System.out.println(json);
    }

}
