package org.jeecg.ftu.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test {

    public static void main(String[] args) {
        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        JSONArray devicesArray = new JSONArray();
        com.alibaba.fastjson.JSONObject device = new com.alibaba.fastjson.JSONObject();
        device.put("deviceId", "设备唯一编码，与业务平台同步");
        JSONArray servicesArray = new JSONArray();
        com.alibaba.fastjson.JSONObject service = new com.alibaba.fastjson.JSONObject();
        service.put("serviceId", "HeartBeat");
        service.put("eventTime", "202507081050");
        com.alibaba.fastjson.JSONObject data = new JSONObject();
        data.put("gps_time", 40001);//发电压
        data.put("running_time", 40002);
        data.put("heart_rate", 40003);

        data.put("system_temp", 40005);//发电线压
        data.put("external_voltage", 40006);
        data.put("internal_voltage", 40007);

        data.put("comm_quality", 40008);//发电电流
        data.put("app_version", 40009);

        service.put("data", data);
        servicesArray.add(service);
        device.put("services", servicesArray);
        devicesArray.add(device);
        json.put("devices", devicesArray);

        JSONObject dataJson = json.getJSONArray("devices")
                .getJSONObject(0)
                .getJSONArray("services")
                .getJSONObject(0)
                .getJSONObject("data");
        Demo demo = dataJson.toJavaObject(Demo.class);
        System.out.println(json);
    }

}
