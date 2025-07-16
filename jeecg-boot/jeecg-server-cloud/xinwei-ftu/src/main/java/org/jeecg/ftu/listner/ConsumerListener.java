//package org.jeecg.ftu.listner;
//
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONObject;
//import com.alibaba.fastjson2.TypeReference;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.jeecg.ftu.entity.FtuDevice;
//import org.jeecg.ftu.entity.FtuF411Device;
//import org.jeecg.ftu.service.IFtuDeviceService;
//import org.jeecg.ftu.service.IFtuF411DeviceService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Objects;
//
//@Component
//public class ConsumerListener {
//
//    @Autowired
//    private IFtuDeviceService deviceService;
//    @Autowired
//    private IFtuF411DeviceService ftuF411DeviceService;
//
//    @KafkaListener(topics = {"${spring.kafka.consumer.topic-name}"})
//    public void message(final ConsumerRecord<String, String> record) {
//        String msg = record.value();
//        msg = msg.replaceAll(" ", "");
//        try {
//            // 解析JSON字符串为JSONObject
//            JSONObject jsonObject = JSON.parseObject(msg);
////            System.out.println(jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).get("serviceId"));
////            System.out.println(jsonObject.getJSONArray("devices"));
//            if(jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).get("serviceId").equals("Location")){
//                double lng  = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getDoubleValue("lng");
//                double lat  = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getDoubleValue("lat");
//                String card  = (String) jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).get("deviceId");
//
//                if(!Objects.isNull(card)){
//                    FtuF411Device f411Device = ftuF411DeviceService.get411Device(card);
//                    ftuF411DeviceService.updateMode(0, f411Device.getId());
//                    deviceService.updateLngLat(lng,lat,0, f411Device.getFtuId());
//                }else{
////                    System.out.println(jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).get("deviceId"));
//                }
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//}
