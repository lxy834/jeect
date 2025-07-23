package org.jeecg.ftu.listnenr;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jeecg.ftu.entity.FtuDevice;
import org.jeecg.ftu.entity.FtuF411Device;
import org.jeecg.ftu.entity.FtuWarnInfo;
import org.jeecg.ftu.service.IFtuDeviceService;
import org.jeecg.ftu.service.IFtuElectlVolumeService;
import org.jeecg.ftu.service.IFtuF411DeviceService;
import org.jeecg.ftu.service.IFtuWarnInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ConsumerListener {

    private static final Logger log = LoggerFactory.getLogger(ConsumerListener.class);
    @Autowired
    private IFtuDeviceService deviceService;
    @Autowired
    private IFtuF411DeviceService ftuF411DeviceService;
    @Autowired
    private IFtuElectlVolumeService volumeService;
    @Autowired
    private IFtuWarnInfoService warnInfoService;

    @KafkaListener(topics = {"${spring.kafka.consumer.topic-name}"})
    public void message(final ConsumerRecord<String, String> record) {
        String msg = record.value();
        msg = msg.replaceAll(" ", "");
        log.info(msg);
        try {
            // 解析JSON字符串为JSONObject
            JSONObject jsonObject = JSON.parseObject(msg);
            String messageType = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).get("serviceId").toString();
            String card = (String) jsonObject.getJSONArray("devices").getJSONObject(0).get("deviceId");
            switch (messageType) {
                case "Location" -> saveLocation(jsonObject, card);
                case "running" -> saveRunning(jsonObject, card);
                case "Communication" -> saveCommunication(jsonObject, card);
                case "CommMode" -> saveCommMode(jsonObject, card);
                default -> log.warn("位置数据：{}", msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveInfo(String warnInfo, String deviceType, String deviceId, String deviceName, String insLocation, String lineLocation, String warnType, String tenantId) {
        FtuWarnInfo info = new FtuWarnInfo();
        info.setWarnInfo(warnInfo);
        info.setDeviceType(warnType);
        info.setDeviceId(deviceId);
        info.setDeviceName(deviceName);
        info.setInsLocation(insLocation);
        info.setLineLocation(lineLocation);
        info.setWarnType(warnType);
        info.setTenantId(tenantId);
        warnInfoService.save(info);
    }

    public void saveLocation(JSONObject jsonObject, String card) {
        double lng = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getDoubleValue("lng");
        double lat = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getDoubleValue("lat");
        if (!Objects.isNull(card)) {
            FtuF411Device f411Device = ftuF411DeviceService.get411Device(card);
            FtuDevice device = deviceService.dataById(f411Device.getFtuId());
            deviceService.updateLngLat(lng, lat, 0, f411Device.getFtuId());
            saveInfo(device.getDeviceName() + "获取定位", "主站指令", device.getId(), device.getDeviceName(), device.getDeviceName(), device.getInsLineName(), "其他", f411Device.getTenantId());
        }
    }

    public void saveRunning(JSONObject jsonObject, String card) {
        double currentFor211OutputB = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getDoubleValue("currentFor211OutputB");
        double uabFor4 = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getDoubleValue("uabFor4");
        Integer sendMode = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getInteger("sendMode");
        FtuF411Device f411Device = ftuF411DeviceService.get411Device(card);
        if (!Objects.isNull(card)) {
            volumeService.insertVolume(uabFor4, currentFor211OutputB, f411Device.getFtuId(), sendMode);
            FtuDevice device = deviceService.dataById(f411Device.getFtuId());
            saveInfo(device.getDeviceName() + "总召唤", "主站指令", device.getId(), device.getDeviceName(), device.getDeviceName(), device.getInsLineName(), "其他", f411Device.getTenantId());
        }
    }

    public void saveCommunication(JSONObject jsonObject, String card) {
        Integer signalFor5G = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getInteger("signalFor5G");
//        Integer statusFor5G = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getInteger("statusFor5G");
        Integer signalForBd = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getInteger("signalForBd");
//        Integer statusForBd = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getInteger("statusForBd");
        Integer statusForFtu = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getInteger("statusForFtu");
        if (!Objects.isNull(card)) {
            ftuF411DeviceService.updateDeviceSignal(signalFor5G, signalForBd, statusForFtu, card);
        }
    }

    public void saveCommMode(JSONObject jsonObject, String card) {
        Integer mode = jsonObject.getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getInteger("commMode");
        if (!Objects.isNull(card)) {
            ftuF411DeviceService.updateMode(mode, card);
        }
    }

}
