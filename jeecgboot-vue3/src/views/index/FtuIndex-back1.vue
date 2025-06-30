<template>
  <div class="dashboard-container">
    <!-- 主内容区 -->
    <div class="main-panel" id="map">
      <!-- 左侧数据卡片 - 重新设计 -->
      <div class="left-stats">
        <!-- 设备状态监测 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-microchip"></i>
            设备统计
          </div>
          <div class="device-stats">
            <div class="stat-item">
              <div class="stat-icon icon-green">
                <i class="fas fa-server"></i>
              </div>
              <div class="stat-value">248</div>
              <div class="stat-label">FTU设备</div>
            </div>
            <div class="stat-item">
              <div class="stat-icon icon-purple">
                <i class="fas fa-wifi"></i>
              </div>
              <div class="stat-value">186</div>
              <div class="stat-label">F411设备</div>
            </div>
            <div class="stat-item">
              <div class="stat-icon icon-blue">
                <i class="fas fa-bolt"></i>
              </div>
              <div class="stat-value">58</div>
              <div class="stat-label">线路条数</div>
            </div>
          </div>
        </div>

        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-clipboard-list"></i>
            事件统计
          </div>
          <div class="event-stats">
            <div class="event-metrics">
              <div class="event-item">
                <div class="stat-icon icon-blue">
                  <i class="fas fa-exclamation-triangle"></i>
                </div>
                <div class="event-value">28</div>
                <div class="event-label">FTU故障</div>
              </div>
              <div class="event-item">
                <div class="stat-icon icon-green">
                  <i class="fas fa-wifi"></i>
                </div>
                <div class="event-value">45</div>
                <div class="event-label">通信掉线</div>
              </div>
              <div class="event-item">
                <div class="stat-icon icon-purple">
                  <i class="fas fa-sliders-h"></i>
                </div>
                <div class="event-value">63</div>
                <div class="event-label">设备操作</div>
              </div>
            </div>
          </div>
        </div>

        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-clipboard-list"></i>
            其他统计（待定）
          </div>
          <div class="event-stats">
            <div class="event-metrics">
              <div class="event-item">
                <div class="stat-icon icon-blue">
                  <i class="fas fa-exclamation-triangle"></i>
                </div>
                <div class="event-value">28</div>
                <div class="event-label">FTU故障</div>
              </div>
              <div class="event-item">
                <div class="stat-icon icon-green">
                  <i class="fas fa-wifi"></i>
                </div>
                <div class="event-value">45</div>
                <div class="event-label">通信掉线</div>
              </div>
              <div class="event-item">
                <div class="stat-icon icon-purple">
                  <i class="fas fa-sliders-h"></i>
                </div>
                <div class="event-value">63</div>
                <div class="event-label">设备操作</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 中央地图区 -->
      <div class="map-container">
        <h2 class="map-container-title">北斗通信电力配网数据采集传输系统</h2>
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
        </div>
      </div>

      <!-- 右侧数据卡片 - 重新设计 -->
      <div class="right-stats">
        <!-- 实时数据传输 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-exchange-alt"></i>
            实时数据传输
          </div>

          <div class="data-table-container">
            <table class="real-time-table">
              <thead>
              <tr>
                <th>编号</th>
                <th>电压 (V)</th>
                <th>电流 (A)</th>
                <th>有功</th>
                <th>功率因数</th>
                <th>通道状态</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td>FTU-01</td>
                <td>226</td>
                <td>28.6</td>
                <td>5.8</td>
                <td>0.92</td>
                <td><span class="channel-badge bg-green">电鸿</span></td>
              </tr>
              <tr>
                <td>FTU-02</td>
                <td>230</td>
                <td>24.8</td>
                <td>4.9</td>
                <td>0.88</td>
                <td><span class="channel-badge bg-blue">北斗</span></td>
              </tr>
              <tr>
                <td>FTU-03</td>
                <td>0</td>
                <td>0</td>
                <td>0</td>
                <td>0</td>
                <td><span class="channel-badge bg-gray">离线</span></td>
              </tr>

              </tbody>
            </table>
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

<!--            <div class="control-item">-->
<!--              <div class="control-label">故障隔离</div>-->
<!--              <div class="control-status">-->
<!--                <i class="fas fa-check-circle status-normal"></i>-->
<!--                正常-->
<!--              </div>-->
<!--              <button class="control-btn">-->
<!--                <i class="fas fa-search"></i> 查看策略-->
<!--              </button>-->
<!--            </div>-->

<!--            <div class="control-item">-->
<!--              <div class="control-label">负荷转供</div>-->
<!--              <div class="control-status">-->
<!--                <i class="fas fa-battery-half status-info"></i>-->
<!--                准备就绪-->
<!--              </div>-->
<!--              <button class="control-btn">-->
<!--                <i class="fas fa-exchange-alt"></i> 转供指令-->
<!--              </button>-->
<!--            </div>-->


          </div>
        </div>
      </div>
    </div>
    <!-- 数据表格区 -->
    <div class="data-panel">
      <table class="data-table">
        <thead>
        <tr>
          <th>类型</th>
          <th>内容</th>
          <th>时间</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td>告警通知</td>
          <td>某某设备离线</td>
          <td>2025-06-24 11:35:56</td>
          <td style="color: deepskyblue;cursor: pointer">处理</td>
        </tr>
        <tr>
          <td>实时数据</td>
          <td>某某设备上传一条数据</td>
          <td>2025-06-24 11:35:56</td>
          <td style="color: deepskyblue;cursor: pointer">详情</td>
        </tr>
        <tr>
          <td>其他通知</td>
          <td>10kV西三Ⅳ回线</td>
          <td>2025-06-24 11:35:56</td>
          <td style="color: deepskyblue;cursor: pointer">详情</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import screenfull from "screenfull";
import { useAppStore } from '@/store/modules/app';

const appStore = useAppStore();
import { onMounted, watchEffect } from "vue";
import { useMenuSetting } from "/@/hooks/setting/useMenuSetting";
const { getShowMenu, setMenuSetting } = useMenuSetting();
import * as echarts from "echarts";
import AMapLoader from "@amap/amap-jsapi-loader";
import "@fortawesome/fontawesome-free/css/all.min.css";
import { onBeforeRouteLeave } from "vue-router";

window._AMapSecurityConfig = {
  securityJsCode: "d1243371803f635fdfa7b253ffb723e0" // 安全密钥
};

watchEffect(() => {
  if (screenfull.isEnabled) {
    screenfull.on("change", () => {
      if (screenfull.isFullscreen) {
        // 全屏时隐藏菜单
        setMenuSetting({ show: false });
      } else {
        // 退出全屏时显示菜单
        setMenuSetting({ show: true });
      }
    });
  }
});
onBeforeRouteLeave(()=>{
  appStore.setLayoutHideHeader(false);
  appStore.setLayoutHideMultiTabs(false);
})
onMounted(() => {
  if (screenfull.isEnabled && !screenfull.isFullscreen) {
    screenfull.request();
  }
  appStore.setLayoutHideSider(true);
  appStore.setLayoutHideHeader(true);
  appStore.setLayoutHideMultiTabs(true);
  // 初始化地图 - 使用真实地理位置展示
  AMapLoader.load({
    key: "e28af433d6fabd84d33509eca1a3efa3",
    version: "2.0",
    plugins: ["AMap.MoveAnimation ", "AMap.DistrictSearch",
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
    const map = new AMap.Map("map", {
      center: [104.830389,26.592528], // 北京中心位置
      zoom: 11,
      mapStyle: "amap://styles/blue" // 使用清新灰色风格地图
    });

    // 添加模拟标记点
    const positions = [
      { position: [104.813738,26.616164], title: "主变电站", status: "active" },
      { position: [104.823738,26.56164], title: "电鸿终端01", status: "active" },
      { position: [104.830389,26.592528], title: "北斗终端04", status: "warning" },
      { position: [104.83789,26.56528], title: "配电中心", status: "active" },
      { position: [104.70389,26.582528], title: "FTU终端23", status: "offline" },
      { position: [104.69,26.692528], title: "FTU终端45", status: "error" },
      { position: [104.810389,26.692528], title: "电鸿终端09", status: "active" }
    ];

    // 根据状态添加不同颜色的标记
    positions.forEach(point => {
      const color =
        point.status === "active" ? "#4ABDAC" :
          point.status === "warning" ? "#F39C12" :
            point.status === "error" ? "#E74C3C" : "#95A5A6";

      new AMap.Marker({
        position: point.position,
        map: map,
        content: `<div style="
          background: ${color};
          width: 20px;
          height: 20px;
          border: 2px solid white;
          border-radius: 50%;
          box-shadow: 0 0 5px rgba(0,0,0,0.3);
        "></div>`,
        offset: new AMap.Pixel(-5, -5)
      });
    });

  }).catch((e) => {
    console.error("地图加载失败:", e);
  });
});
</script>

<style lang="less" scoped>
/* 这里复制原 HTML 文件中的样式 */
.amap-copyright {
  opacity: 0;
}

.amap-container {
  background-image: none
}

.amap-marker-label {
  background: none;
  border: none;
}

::v-deep .amap-logo {
  display: none;
  opacity: 0 !important;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Microsoft YaHei', 'PingFang SC', 'Segoe UI', sans-serif;
}

:root {
  --dark-bg: #0A0F16;
  --panel-bg: rgba(15, 26, 37, 0.7);
  --panel-border: rgba(30, 145, 255, 0.3);
  --text-primary: #FFFFFF;
  --text-secondary: #9BA3A9;
  --electric-blue: #2F89FC;
  --tele-green: #41C23C;
  --warning-yellow: #FFC600;
  --danger-red: #FF3636;
  --accent-color: #00F0FF;
  --panel-spacing: 5px;
}

body {

  color: white;
  overflow-x: hidden;

  min-height: 100vh;
  width: 100vw;
}

.dashboard-container {
  background-image: url("https://api3.boot.jeecg.com/drag/lib/img/bg2.png");
  background-size: cover;
  background-repeat: no-repeat;
  display: grid;
  grid-template-areas:
    "header"
    "main"
    "data";
  //color: white;
  //padding: 0;
  grid-template-rows: auto auto auto auto;
  min-height: 100vh;
  padding:10px;
  gap: var(--panel-spacing);
}

/* 头部样式 */
.header-panel {
  grid-area: header;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  background: rgba(10, 20, 40, 0.5);
  border-radius: 12px;
  padding: 5px;
  border: 1px solid rgba(30, 145, 255, 0.2);
}

.header-title {
  font-size: 2rem;
  font-weight: 700;
  color: #FFFFFF;
  text-shadow:
    0 0 10px rgba(47, 137, 252, 0.8),
    0 0 20px rgba(47, 137, 252, 0.6);
  letter-spacing: 2px;
  text-align: center;
}

.header-title::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 25%;
  width: 50%;
  height: 3px;
  background: linear-gradient(90deg, transparent, #00F0FF, transparent);
}

/* 主内容区样式 */
.main-panel {
  z-index: 1;
  grid-area: main;
  display: flex;
  flex-wrap: wrap;
  //gap: var(--panel-spacing);
  //max-height: 750px;
  min-height: 400px; /* 从500px减小20% */
}

.left-stats,
.right-stats {
  z-index: 100;
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 300px;
  max-height: 750px;
  min-height: 400px;
  gap: 5px;
}

.map-container {
  flex: 2;
  background: var(--panel-bg);
  border: 1px solid var(--panel-border);
  border-radius: 12px;
  padding: 15px;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.3);
  //min-height: 200px; /* 从500px减小20% */
}

.map-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #00F0FF, transparent);
}

.map-container-title {
  z-index: 100;
  font-size: 2.5rem;
  margin-bottom: 10px;
  color: #00F0FF;
  text-align: center;
  letter-spacing: 1px;
}

#map {
  flex-grow: 1;
  min-height: 400px; /* 从300px减小20% */
}

.map-footer {
  z-index: 100;
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 15px;
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

.offline-bg {
  background: #9BA3A9;
}

/* 重新设计的卡片样式 */
.stat-card {
  background: rgba(15, 26, 37, 0.8);
  //border: 1px solid  rgba(30, 145, 255, 0.3);
  border-radius: 12px;
  padding: 10px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  min-height: 250px;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 240, 255,1);
}

.stat-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #00F0FF, transparent);
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

.stat-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 15px auto;
  border-radius: 10px;
  font-size: 1.3rem;
}

.icon-blue {
  background: rgba(47, 137, 252, 0.2);
  color: #2F89FC;
}

.icon-green {
  background: rgba(65, 194, 60, 0.2);
  color: #41C23C;
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
  font-size: 1.8rem;
  font-weight: bold;
  margin: 10px 0;
  color: #00F0FF;
}

.event-label {
  font-size: 0.9rem;
  color:white;
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
  color: #00F0FF;
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

/* 重新设计的指标卡片 */
.metric-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.metric-item {
  background: rgba(20, 33, 51, 0.6);
  border-radius: 10px;
  padding: 15px;
  display: flex;
  flex-direction: column;
  position: relative;
  transition: all 0.3s ease;
  border: 1px solid rgba(47, 137, 252, 0.1);
}

.metric-item:hover {
  border-color: var(--electric-blue);
  background: rgba(25, 45, 70, 0.6);
}

.metric-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.metric-icon {
  font-size: 1.2rem;
  margin-right: 10px;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(47, 137, 252, 0.2), transparent);
}

.metric-name {
  font-size: 0.9rem;
  color: var(--text-secondary);
}

.metric-value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #FFFFFF;
  margin: 5px 0;
}

.metric-status {
  font-size: 0.85rem;
  padding: 4px 10px;
  border-radius: 20px;
  text-align: center;
  align-self: flex-start;
  margin-top: 5px;
  transition: all 0.3s ease;
}

.status-normal {
  //background: rgba(65, 194, 60, 0.15);
  color: #41C23C;
  //border: 1px solid rgba(65, 194, 60, 0.3);
}

.status-warning {
  //background: rgba(255, 198, 0, 0.15);
  color: #FFC600;
  //border: 1px solid rgba(255, 198, 0, 0.3);
}

.status-error {
  //background: rgba(255, 54, 54, 0.15);
  color: #FF3636;
  //border: 1px solid rgba(255, 54, 54, 0.3);
}

.status-info {
  //background: rgba(47, 137, 252, 0.15);
  color: #2F89FC;
  //border: 1px solid rgba(47, 137, 252, 0.3);
}

/* 进度条样式 */
.progress-container {
  height: 6px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
  margin: 10px 0;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  border-radius: 3px;
}

.progress-normal {
  background: var(--tele-green);
}

.progress-warning {
  background: var(--warning-yellow);
}

.progress-danger {
  background: var(--danger-red);
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
  //margin-bottom: 10px;
  color: #9BA3A9;
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

/* 故障信息区 */
.fault-panel {
  grid-area: fault;
  display: flex;
  flex-wrap: wrap;
  gap: var(--panel-spacing);
  min-height: 144px; /* 从180px减小20% */
}

.fault-card {
  background:rgba(15, 26, 37, 0.7);
  border: 1px solid  rgba(30, 145, 255, 0.3);
  border-radius: 12px;
  padding: 20px;
  flex: 1;
  min-width: 400px;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

.fault-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #00F0FF, transparent);
}

.fault-title {
  font-size: 1.1rem;
  color: #00F0FF;
  margin-bottom: 15px;
}

.fault-stats {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
  flex-grow: 1;
  align-items: center;
}

.fault-stat {
  min-width: 180px;
}

.fault-label {
  font-size: 0.9rem;
  color: #9BA3A9;
}

.fault-value {
  font-size: 1.2rem;
  color: white;
  margin-top: 5px;
}

.normal {
  color: #41C23C;
}

.divider {
  width: 1px;
  background: rgba(47, 137, 252, 0.2);
  margin: 0 20px;
}

/* 数据表格区 */
.data-panel {
  color: white;
  grid-area: data;
  background: rgba(15, 26, 37, 0.7);
  border: 1px solid rgba(30, 145, 255, 0.3);
  border-radius: 12px;
  padding: 10px;
  position: relative;
  max-height: 200px; /* 从450px减小20% */
  overflow-y: auto;
}

.data-panel::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #00F0FF, transparent);
}

.data-title {
  font-size: 1.2rem;
  color: #00F0FF;
  margin-bottom: 5px;
}

.data-subtitle {
  font-size: 0.85rem;
  color: var(--text-secondary);
  margin-bottom: 15px;
}

/* 设置滚动条整体样式 */
::-webkit-scrollbar {
  width: 20px; /* 滚动条宽度 */
  height: 20px; /* 滚动条高度 */
}

/* 设置滚动条轨道为透明 */
::-webkit-scrollbar-track {
  background-color: transparent;
}

/* 设置滚动条滑块为透明 */
::-webkit-scrollbar-thumb {
  background-color: transparent;
}

/* 鼠标悬停时滑块略微可见 */
::-webkit-scrollbar-thumb:hover {
  background-color: rgba(0, 0, 0, 0.2);
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

.data-table th {
  background: rgba(25, 35, 51, 0.8);
  padding: 12px 15px;
  text-align: left;
  color: #00F0FF;
  font-weight: normal;
  position: sticky;
  top: 0;
}

.data-table td {
  padding: 10px 15px;
  border-bottom: 1px solid rgba(0, 240, 255, 0.1);
}

.data-table tr:hover {
  background: rgba(30, 60, 100, 0.4);
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
  background: rgba(65, 194, 60, 0.2);
}

.bg-blue {
  background: rgba(47, 137, 252, 0.2);
}

.bg-gray {
  background: rgba(155, 163, 169, 0.2);
}

.bg-red {
  background: rgba(255, 54, 54, 0.2);
}

.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

/* 响应式设计 */
@media (max-width: 1400px) {
  .fault-stat {
    min-width: 150px;
  }

  .fault-card {
    min-width: 300px;
  }
}

@media (max-width: 1200px) {
  .left-stats,
  .right-stats {
    min-width: 250px;
  }

  .map-container {
    min-width: 400px;
  }

  .fault-card {
    min-width: 250px;
  }

  .metric-grid,
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
    height: 240px; /* 从300px减小20% */
  }

  .fault-stat {
    min-width: 120px;
  }

  .header-title {
    font-size: 2rem;
  }
}

@media (max-width: 768px) {
  .map-footer {
    flex-direction: column;
    align-items: center;
  }

  .fault-stats {
    gap: 15px;
  }

  .fault-stat {
    min-width: 100px;
  }

  .header-title {
    font-size: 1.7rem;
  }
}
</style>
