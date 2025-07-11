<template>
  <div class="dashboard-container">
    <div class="header-panel">
      <div style="text-align: right;width: 100%">
        <div class="qp icon-white">
          <i class="fas fa-expand"  v-show="state.show" @click="exit(true)"/>
          <i class="fa fa-compress" v-show="!state.show" @click="exit(false)"/>
        </div>
      </div>
    </div>
    <!-- 主内容区 -->
    <div class="main-panel" id="map">
      <div class="left-stats">
        <!-- 馈线信息 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-exchange-alt"></i>
            馈线信息
          </div>
          <table class="real-time-table">
            <thead>
            <tr class="head">
              <th>终端名称</th>
              <th>馈线名称</th>
            </tr>
            </thead>
          </table>
          <div>
            <vue3ScrollSeamless
              style="margin: 0 auto; overflow: auto;position: absolute;"
              :hover="true"
              :wheel="true"
              :isWatch="true"
              :classOptions="classOptions"
              :dataList="state.ftuDeviceList"
            >
              <table class="real-time-table">
                <thead>
                <tr class="head"></tr>
                <tr v-for="(item,i) of state.ftuDeviceList" :key="i">
                  <td>{{item.deviceName}}</td>
                  <td>{{item.insLineName}}</td>
                </tr>
                </thead>
              </table>
            </vue3ScrollSeamless>
          </div>
        </div>
        <!-- 信道信息 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-microchip"></i>
            信道信息
          </div>
          <div class="device-stats">
            <div class="stat-item">
              <div class="stat-icon icon-blue">
                <i class="fas fa-satellite" />
              </div>
              <div class="stat-label">北斗模式</div>
              <div class="stat-value">{{state.bdCount}}</div>
            </div>
            <div class="stat-item">
              <div class="stat-icon icon-green">
                <i class="fas fa-broadcast-tower" />
              </div>
              <div class="stat-label">电鸿模式</div>
              <div class="stat-value">{{state.dhCount}}</div>
            </div>
            <div class="stat-item">
              <div class="stat-icon icon-purple">
                <i class="fas fa-signal" />
              </div>
              <div class="stat-label">混合模式</div>
              <div class="stat-value">{{state.hhCount}}</div>
            </div>
          </div>
        </div>
        <!-- 设备事件 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-clipboard-list"></i>
            设备事件
          </div>
          <div class="event-stats">
            <div class="event-metrics">
              <div class="event-item">
                <div class="stat-icon icon-gray">
                  <i class="fas fa-tasks"></i>
                </div>
                <div class="event-label">FTU掉电</div>
                <div class="event-value">{{state.statForm.powerDown}}</div>
              </div>
              <div class="event-item">
                <div class="stat-icon icon-orange">
                  <i class="fas fa-line-chart" />
                </div>
                <div class="event-label">FTU故障</div>
                <div class="event-value">{{state.statForm.fault}}</div>
              </div>
              <div class="event-item">
                <div class="stat-icon icon-red">
                  <i class="fas fa-exclamation-triangle"></i>
                </div>
                <div class="event-label">掉线</div>
                <div class="event-value">{{state.statForm.disconnect}}</div>
              </div>
              <div class="event-item">
                <div class="stat-icon icon-purple">
                  <i class="fas fa-sliders-h"></i>
                </div>
                <div class="event-label">其它</div>
                <div class="event-value">{{state.statForm.otherEvent}}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 中央地图区 -->
      <div class="map-container">
        <div class="map-footer">
          <div class="status-item">
            <span class="status-dot tele-green-bg"></span>
            <span>电鸿通道工作</span>
          </div>
          <div class="status-item">
            <span class="status-dot electric-blue-bg"></span>
            <span>北斗通道工作</span>
          </div>
          <div class="status-item">
            <span class="status-dot offline-bg"></span>
            <span>通信一体机离线</span>
          </div>
          <div class="status-item">
            <span class="status-dot warning-bg"></span>
            <span>FTU故障但通信正常</span>
          </div>
          <div class="status-item">
            <span class="status-dot danger-bg"></span>
            <span>FTU告警</span>
          </div>
          <div class="status-item">
            <span class="status-dot hh-bg"></span>
            <span>混合通信模式</span>
          </div>
        </div>
      </div>
      <!-- 右侧数据卡片 - 重新设计 -->
      <div class="right-stats">
        <!-- 实时数据传输 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-exchange-alt"></i>
            实时数据
          </div>
          <div class="data-table-container" ref="realTimeTableContainer">
            <table class="real-time-table">
              <thead>
              <tr>
                <th>线路</th>
                <th>电压</th>
                <th>电流</th>
                <th>有功</th>
                <th>功率因数</th>
                <th>数据通道</th>
              </tr>
              </thead>
              <tbody></tbody>
            </table>
            <vue3ScrollSeamless
              style="margin: 0 auto; overflow: hidden;max-height: 100%;position: absolute"
              :hover="true"
              :wheel="true"
              :isWatch="true"
              :classOptions="classOptions"
              :dataList="state.volumeList"
            >
              <table class="real-time-table">
                <thead>
                <tr class="head"></tr>
                <tr v-for="(item,i) of state.volumeList" :key="i">
                  <td>{{item.insLineName}}</td>
                  <td>{{item.voltage}}V</td>
                  <td>{{item.ftuCurrent}}A</td>
                  <td>{{item.activePower}}</td>
                  <td>{{item.factor}}</td>
                  <td>
                    <span class="channel-badge bg-green" v-if="item.sendMode==='电鸿'">电鸿</span>
                    <span class="channel-badge bg-blue" v-else>北斗</span>
                  </td>
                </tr>
                </thead>
              </table>
            </vue3ScrollSeamless>
          </div>
        </div>
        <!-- 控制功能 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-sliders-h"></i>
            终端信息
          </div>
          <div class="control-grid">
            <div class="control-item">
              <div class="control-label">设备状态</div>
              <div class="control-status">
                <i class="fas fa-check-circle status-normal"></i>
                正常
              </div>
              <div class="btn-group">
                <button class="control-btn">
                  <i class="fas fa-search"></i> 更多信息
                </button>
              </div>
            </div>
            <div class="control-item">
              <div class="control-label">FTU对时</div>
              <div class="control-status">
                <i class="fas fa-satellite status-info"></i>
                北斗授时
              </div>
              <button class="control-btn">
                <i class="fas fa-exchange-alt"></i> 同步对时
              </button>
            </div>
            <div class="control-item">
              <div class="control-label">电鸿状态</div>
              <div class="control-status">
                <i class="fas fa-check-circle status-normal"></i>
                正常
              </div>
            </div>
            <div class="control-item">
              <div class="control-label">北斗状态</div>
              <div class="control-status">
                <i class="fas fa-check-circle status-normal"></i>
                正常
              </div>
            </div>
          </div>
        </div>
        <!-- 告警通知 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-exchange-alt"></i>
            告警通知
          </div>
          <table class="real-time-table">
            <thead>
            <tr class="head">
              <th>类型</th>
              <th>内容</th>
              <th>时间</th>
            </tr>
            </thead>
          </table>
          <div>
            <vue3ScrollSeamless
              style="margin: 0 auto; overflow: auto;position: absolute;"
              :hover="true"
              :wheel="true"
              :isWatch="true"
              :classOptions="classOptions"
              :dataList="state.warnList"
            >
              <table class="real-time-table">
                <thead>
                <tr class="head"></tr>
                <tr v-for="(item,i) of state.warnList" :key="i">
                  <td>{{item.deviceType}}</td>
                  <td>{{item.warnInfo}}</td>
                  <td>{{item.warnTime}}</td>
                </tr>
                </thead>
              </table>
            </vue3ScrollSeamless>
          </div>
        </div>
      </div>
      <el-dialog  v-model="state.dialogVisible" draggable>
        <div id="main" style="height: 400px;width: 100%;  background: rgba(14, 38, 59, 0.9);color: white"></div>
        <div id="stat" style="height: 400px;width: 100%;  background: rgba(14, 38, 59, 0.9);color: white"></div>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import screenfull from "screenfull";
import { useAppStore } from "@/store/modules/app";
import { onMounted, reactive, ref, watchEffect } from "vue";
import { useMenuSetting } from "/@/hooks/setting/useMenuSetting";
import AMapLoader from "@amap/amap-jsapi-loader";
import "@fortawesome/fontawesome-free/css/all.css";
import { onBeforeRouteLeave } from "vue-router";
import { vue3ScrollSeamless } from "vue3-scroll-seamless";
import { ftuF411DeviceList, list, volume } from "@/views/ftu/list/FtuDevice.api.ts";
import { defHttp } from "@/utils/http/axios";
import { getWarnList, eventStat } from "@/views/ftu/warn/FtuWarnInfo.api";
import { getStat } from "@/views/ftu/f411/FtuF411Device.api.ts";
import { useGo } from '/@/hooks/web/usePage';

const appStore = useAppStore();
const {  setMenuSetting } = useMenuSetting();
window._AMapSecurityConfig = {
  securityJsCode: "d1243371803f635fdfa7b253ffb723e0" // 安全密钥
};

const realTimeTableContainer = ref(null);

const state = reactive({
  show: true,
  dialogVisible: false,
  ftuDeviceList: [],
  volumeList: [],
  bdCount: 0,
  dhCount: 0,
  hhCount: 0,
  warnList: [],
  statForm: {}
});

async function getList() {
  const pageParams = {
    order: 'desc',
    pageNo: 1,
    pageSize: 10
  };

  // 并行处理无依赖的请求
  const [deviceRes, volumeRes, warnRes, statRes, eventRes] = await Promise.all([
    list(pageParams),
    defHttp.get({ url: volume }),
    getWarnList(pageParams),
    defHttp.get({ url: getStat }),
    defHttp.get({ url: eventStat })
  ]);

  // 处理设备列表和初始化地图
  state.ftuDeviceList = deviceRes.records;
  initMap();

  // 处理容量列表
  state.volumeList = volumeRes;

  // 处理告警列表
  state.warnList = warnRes.records;

  // 使用Map高效统计通信模式数量
  const commModeMap = new Map([
    [0, 'bdCount'],
    [1, 'dhCount'],
    [2, 'hhCount']
  ]);

  statRes.forEach(item => {
    const countKey = commModeMap.get(item.COMMUNICATION_MODE);
    if (countKey) {
      state[countKey] = (state[countKey] || 0) + 1;
    }
  });

  // 处理事件统计
  state.statForm = eventRes;
}

function initMap() {
  // 预定义状态映射和颜色映射
  const statusMap = {
    0: { status: "dh_active", commStatus: "北斗在线", color: "#2F89FC" },
    1: { status: "bd_active", commStatus: "电鸿在线", color: "#41C23C" },
    2: { status: "f411_offline", commStatus: "一体机离线", color: "#9BA3A9" },
    3: { status: "ftu_warning", commStatus: "电鸿在线", color: "#FFC600" },
    4: { status: "error", commStatus: "电鸿在线", color: "#FF3636" },
    5: { status: "hh_comm", commStatus: "混合模式", color: "#6633ff" }
  };

  // 加载地图库
  AMapLoader.load({
    key: "e28af433d6fabd84d33509eca1a3efa3",
    version: "2.0",
    plugins: [
      "AMap.MoveAnimation",
      "AMap.DistrictSearch",
      "AMap.ToolBar",
      "AMap.Driving",
      "AMap.PolygonEditor",
      "AMap.PolylineEditor",
      "AMap.MouseTool",
      "AMap.PlaceSearch",
      "AMap.DistrictSearch",
      "AMap.MarkerClusterer"
    ]
  }).then((AMap) => {
    // 创建地图实例
    const map = new AMap.Map("map", {
      center: [104.830389, 26.592528],
      zoom: 11,
      mapStyle: "amap://styles/d86da4c2ed42be8272eb068059df8719"
    });

    // 合并数据处理和标记创建
    const markers = state.ftuDeviceList.map(device => {
      const statusInfo = statusMap[device.status] || statusMap[2]; // 默认使用离线状态

      const markerData = {
        position: [device.lng, device.lat],
        deviceName: device.deviceName,
        insLineName: device.insLineName,
        id: device.id,
        ...statusInfo
      };

      // 创建标记
      const marker = new AMap.Marker({
        position: markerData.position,
        content: `<div style="
          background: ${markerData.color};
          width: 20px;
          height: 20px;
          border: 2px solid white;
          border-radius: 50%;
          box-shadow: 0 0 5px rgba(0,0,0,0.3);
        "></div>`,
        offset: new AMap.Pixel(-10, -20)
      });

      // 绑定点击事件
      marker.on("click", async () => {
        try {
          const data = await defHttp.get({url: ftuF411DeviceList,params: { id: markerData.id }});

          // 创建信息窗口内容
          const infoWindowContent = `
            <div style="background: black">
              <div style="background-image: url('https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/17.png');height: 250px;width: 500px;background-repeat: no-repeat;background-size: cover">
                <div style="width: 100%;text-align: right;color: white;font-weight: bold">${markerData.deviceName}</div>
                <div style="height: 84%;width: 100%;display: flex">
                  <div style="width: 60%;height: 100%;color: rgba(99, 242, 255, 1)">
                    <div style="height: 24%;margin-top: 4%;margin-left: 2%">设备名称：${markerData.deviceName}</div>
                    <div style="height: 18%;margin-top: 2%;margin-left: 2%">线路名称：${markerData.insLineName}</div>
                    <div style="height: 18%;margin-top: 2%;margin-left: 2%">设备编码：${data[0]?.deviceCode || '未知'}</div>
                    <div style="height: 18%;margin-top: 2%;margin-left: 2%">通信状态：${markerData.commStatus}</div>
                    <div style="height: 12%;margin-top: 2%;margin-left: 2%">历史数据：<span class="info-content" style="cursor: pointer;text-decoration: underline;color: white;font-weight: bold">查看</span></div>
                  </div>
                  <div style="width: 40%;height: 96%;margin-top:4%">
                    <img src='https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/ftu.jpg' style='width: 100%;height:100%'/>
                  </div>
                </div>
              </div>
            </div>
          `;

          // 创建或更新信息窗口
          let infoWindow = marker.infoWindow;
          if (!infoWindow) {
            infoWindow = new AMap.InfoWindow({
              offset: new AMap.Pixel(0, -15),
              content: infoWindowContent
            });
            marker.infoWindow = infoWindow; // 存储在marker上，避免重复创建
          } else {
            infoWindow.setContent(infoWindowContent);
          }

          infoWindow.open(map, marker.getPosition());

          // 绑定历史数据点击事件
          setTimeout(() => {
            const infoWindowContent = document.querySelector('.info-content');
            if (infoWindowContent) {
              infoWindowContent.onclick = () => goData(markerData);
            }
          }, 0);

        } catch (error) {
          console.error('获取设备详情失败:', error);
        }
      });

      return marker;
    });

    // 添加所有标记到地图
    map.add(markers);

    // 全局点击事件处理
    map.on("click", (e) => {
      if (!(e.target instanceof AMap.Marker)) {
        // 关闭所有信息窗口
        markers.forEach(marker => {
          if (marker.infoWindow) {
            marker.infoWindow.close();
          }
        });
      }
    });

  }).catch((e) => {
    console.error("地图加载失败:", e);
  });
}

const classOptions = {
  limitMoveNum: 3,
  step: 0.1
};

const go = useGo();

function goData(v) {
  go({
    path: '/index/componets',
    query: {
      ftu: v.id,
      device: v.deviceName
    }
  });
}

function exit(v) {
  if (v) {
    state.show = false;
    screenfull.request();
    setMenuSetting({ show: false });
    appStore.setLayoutHideHeader(true);
    appStore.setLayoutHideSider(true);
    appStore.setLayoutHideMultiTabs(true);
  } else {
    state.show = true;
    setMenuSetting({ show: true });
    appStore.setLayoutHideHeader(false);
    appStore.setLayoutHideMultiTabs(false);
    appStore.setLayoutHideSider(false);
  }
}

watchEffect(() => {
  if (screenfull.isEnabled) {
    screenfull.on("change", () => {
      if (screenfull.isFullscreen) {
        state.show = false;
        // 全屏时隐藏菜单
        setMenuSetting({ show: false });
      } else {
        state.show = true;
        // 退出全屏时显示菜单
        setMenuSetting({ show: true });
        appStore.setLayoutHideHeader(false);
        appStore.setLayoutHideMultiTabs(false);
        appStore.setLayoutHideSider(false);
      }
    });
  }
});

onBeforeRouteLeave(() => {
  appStore.setLayoutHideHeader(false);
  setMenuSetting({ show: true });
  appStore.setLayoutHideMultiTabs(false);
  appStore.setLayoutHideSider(false);
});

onMounted(() => {
  document.addEventListener('keydown', function(event) {
    if (event.keyCode === 27) { // 27是ESC键的keyCode
      setMenuSetting({ show: true });
      appStore.setLayoutHideHeader(false);
      appStore.setLayoutHideMultiTabs(false);
      appStore.setLayoutHideSider(false);
      state.show = true;
    }
  });
  getList();
  setMenuSetting({ show: true });
  appStore.setLayoutHideHeader(true);
  appStore.setLayoutHideMultiTabs(true);
  appStore.setLayoutHideSider(true);
  state.show = true;
});
</script>

<style lang="less" scoped>
.amap-copyright {
  opacity: 0;
}

.amap-container {
  background-image: none;
}

::v-deep .amap-marker-label {
  padding: 0;
  background: red;
  border: 3px solid white;
}

::v-deep .amap-logo {
  display: none;
  opacity: 0 !important;
}

body {
  color: white;
  overflow: auto;
  min-height: 100vh;
  width: 100vw;
}

.dashboard-container {
  background-image: url("https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/bg_ftu.png");
  background-size: cover;
  position: relative;
  z-index: 200;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  gap: 5px;
}

/* 头部样式 */
.header-panel {
  background-image: url("https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/xtmc.png");
  background-repeat: no-repeat;
  background-size: contain;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  height: 60px;
  border-radius: 12px;
}

/* 主内容区样式 */
.main-panel {
  flex-grow: 1;
  z-index: 100;
  display: flex;
  flex-wrap: wrap;
}

.left-stats,
.right-stats {
  z-index: 100;
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 300px;
  gap: 5px;
}

.left-stats .stat-card,
.right-stats .stat-card {
  flex: 1;
  min-height: 300px;
}

.map-container {
  bottom: 0;
  flex: 2;
  background: var(--panel-bg);
  border: 1px solid var(--panel-border);
  border-radius: 12px;
  padding: 15px;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

.map-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
}

#map {
  flex-grow: 1;
  min-height: 400px;
}

.map-footer {
  z-index: 100;
  display: flex;
  justify-content: center;
  gap: 15px;
  border: 1px;
  height: 40px;
  font-weight: bold;
  background-color: rgba(0, 0, 0, 0.75);
  margin-top: 100%;
  flex-wrap: wrap;
}

.status-item {
  color: white;
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.9rem;
}

.status-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}

.tele-green-bg {
  background: #41C23C;
}

.electric-blue-bg {
  background: #2F89FC;
}

.warning-bg {
  background: #FFC600;
}

.danger-bg {
  background: #FF3636;
}

.hh-bg {
  background: #6633ff;
}

.offline-bg {
  background: #9BA3A9;
}

/* 重新设计的卡片样式 */
.stat-card {
  background: rgba(14, 38, 59, 0.9);
  border-radius: 12px;
  padding: 10px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgb(0, 119, 255);
}

.stat-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #0080ff, transparent);
}

/* 设备统计样式 */
.device-stats {
  display: flex;
  justify-content: space-between;
  padding: 0 5px;
  height: 100%;
}

.stat-item {
  text-align: center;
  padding: 10px;
  flex: 1;
}

.stat-value {
  font-size: 2.5rem;
  font-weight: bold;
  margin: 10px 0 5px;
  color: #00F0FF;
}

.stat-label {
  font-size: 1rem;
  color: white;
}

.qp {
  float: right;
  cursor: pointer;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 15px auto;
  border-radius: 10px;
  font-size: 1.3rem;
}

.stat-icon {
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 15px auto;
  border-radius: 10px;
  font-size: 1.3rem;
}

.icon-white {
  background: rgba(47, 137, 252, 0);
  color: #ffffff;
}

.icon-blue {
  background: rgba(47, 137, 252, 0.2);
  color: #2F89FC;
}

.icon-red {
  background: rgba(252, 47, 47, 0.2);
  color: #fd2828;
}

.icon-green {
  background: rgba(65, 194, 60, 0.2);
  color: #41C23C;
}

.icon-gray {
  background: rgba(128, 128, 128, 0.2);
  color: #808080;
}

.icon-purple {
  background: rgba(143, 70, 239, 0.2);
  color: #A855F7;
}

.icon-orange {
  background: rgba(255, 140, 0, 0.2);
  color: #FF8C00;
}

/* 事件统计样式 */
.event-stats {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
}

.event-metrics {
  display: flex;
  justify-content: space-around;
  height: 60%;
  padding: 10px 0;
}

.event-item {
  text-align: center;
  flex: 1;
  padding: 10px;
}

.event-value {
  font-size: 2.5rem;
  font-weight: bold;
  margin: 10px 0;
  color: #00F0FF;
}

.event-label {
  font-size: 1rem;
  color: white;
}

/* 表格样式 */
.real-time-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  font-size: 0.9rem;
}

.real-time-table th {
  background: rgba(25, 35, 51, 0.8);
  padding: 12px 0px;
  text-align: center;
  color: #00F0FF;
  font-weight: normal;
  position: sticky;
  top: 0;
}

.real-time-table td {
  padding: 10px 0px;
  color: white;
  text-align: center;
  border-bottom: 1px solid rgba(0, 240, 255, 0.1);
}

.data-table-container {
  height: calc(100% - 40px);
  overflow-y: auto;
}

.real-time-table th:first-child,
.real-time-table td:first-child {
  width: 40px;
}

.real-time-table th:nth-child(2),
.real-time-table th:nth-child(3),
.real-time-table th:nth-child(4),
.real-time-table th:nth-child(5) {
  width: 40px;
}

.real-time-table th:last-child {
  width: 40px;
}

.card-title {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
}

.card-title i {
  margin-right: 10px;
  font-size: 1.2rem;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 240, 255, 0.15);
}

/* 控制功能卡片 */
.control-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.control-item {
  background: rgba(20, 33, 51, 0.3);
  border-radius: 10px;
  padding: 15px;
  text-align: center;
}

.control-label {
  font-size: 0.95rem;
  color: white;
}

.control-status {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 10px 0;
  color: white;
  min-height: 35px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.control-btn {
  padding: 8px 15px;
  background: rgba(25, 35, 51, 0.5);
  color: #00F0FF;
  border: 1px solid #2F89FC;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.9rem;
  margin-top: 10px;
  width: 100%;
}

.control-btn:hover {
  background: #2F89FC;
  color: white;
}

.channel-badge {
  padding: 3px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  display: inline-block;
  min-width: 80px;
  text-align: center;
}

.bg-green {
  background: rgba(65, 194, 60, 0.7);
}

.bg-blue {
  background: rgba(47, 137, 252, 0.7);
}

.status-normal {
  color: #41C23C;
}

.status-info {
  color: #2F89FC;
}

/* 响应式设计 */
@media (max-width: 1400px) {
  .left-stats,
  .right-stats {
    min-width: 250px;
  }

  .map-container {
    min-width: 400px;
  }

  .control-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 992px) {
  .main-panel {
    flex-direction: column;
  }

  .map-container {
    min-width: 100%;
    height: 240px;
  }
}

@media (max-width: 768px) {
  .map-footer {
    flex-direction: column;
    align-items: center;
  }
}

::v-deep .amap-info-content {
  padding: 0;
}

::v-deep .el-dialog__header {
  background: rgba(14, 38, 59, 0.9);
}

::v-deep .el-dialog {
  background: rgba(14, 38, 59, 0.9);
}

::v-deep .amap-info-close {
  display: none;
}
</style>
