<template>
  <div class="fdq_container">
    <div class="header-panel">
    </div>
    <!-- 主内容区 -->
    <div class="fdq_main-panel" id="fdq_map">

      <div class="left-stats">
        <!-- 任务清单 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-clipboard-list"></i>
            任务清单
          </div>
          <div></div>
        </div>
        <!-- 任务详情 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-microchip"></i>
            任务详情
          </div>
          <div class="device-stats"></div>
        </div>
      </div>
      <!-- 中央地图区 -->
      <div class="map-container">
        <div class="map-footer">
          <div class="status-item">
            <span class="status-dot tele-green-bg"></span>
            <span>正在发电</span>
          </div>
          <div class="status-item">
            <span class="status-dot electric-blue-bg"></span>
            <span>车辆驻留</span>
          </div>
          <div class="status-item">
            <span class="status-dot orange-bg"></span>
            <span>正在移动</span>
          </div>
          <div class="status-item">
            <span class="status-dot offline-bg"></span>
            <span>通信掉线</span>
          </div>
        </div>
        <!-- 新增容器 -->
        <div class="new-container">
          <div class="card-title" >
            <i class="fas fa-exclamation-triangle"></i>
            告警信息
          </div>
        </div>
      </div>
      <!-- 右侧数据卡片 - 重新设计 -->
      <div class="right-stats">
        <!-- 实时数据传输 -->
        <div class="stat-card">
          <div class="card-title" >
            <i class="fas fa-clipboard-list"></i>
            维护清单
          </div>
        </div>
        <!-- 控制功能 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-sliders-h"></i>
            维护台账
          </div>
          <div class="control-grid"></div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watchEffect } from "vue";
import { useAppStore } from "@/store/modules/app";
import { useMenuSetting } from "@/hooks/setting/useMenuSetting";
import AMapLoader from "@amap/amap-jsapi-loader";
const appStore = useAppStore();
const {  setMenuSetting } = useMenuSetting();
window._AMapSecurityConfig = {
  securityJsCode: "d1243371803f635fdfa7b253ffb723e0" // 安全密钥
};
onMounted(() => {
  document.addEventListener('keydown', function(event) {
    if (event.keyCode === 27) { // 27是ESC键的keyCode
      setMenuSetting({ show: true });
      appStore.setLayoutHideHeader(false);
      appStore.setLayoutHideMultiTabs(false);
      appStore.setLayoutHideSider(false);
    }
  });
  setMenuSetting({ show: true });
  appStore.setLayoutHideHeader(true);
  appStore.setLayoutHideMultiTabs(true);
  appStore.setLayoutHideSider(true);

  initMap()
});


function initMap() {
  AMapLoader.load({
    key: "e28af433d6fabd84d33509eca1a3efa3",
    version: "2.0",
    plugins: [
      "AMap.DistrictSearch", // 仅需保留区域搜索插件
    ]
  }).then((AMap) => {
    let bounds
    const districtSearch = new AMap.DistrictSearch({
      subdistrict: 0, // 不显示下级行政区划
      extensions: "all", // 返回边界坐标
      level: "province" // 搜索级别为省级
    });
    let mask = []
    districtSearch.search('贵州省', function(status, result) {
      if (status === 'complete' && result.districtList.length) {
        bounds = result.districtList[0].boundaries;
        if (bounds) {
          for (let i = 0; i < bounds.length; i++) {
            mask.push([bounds[i]])
          }
          const map = new AMap.Map("fdq_map", {
            mask: mask,
            center: [106.6172, 26.5783], // 贵州省大致中心点
            zoom: 7.5, // 初始缩放级别
            mapStyle: "amap://styles/d86da4c2ed42be8272eb068059df8719"
          });
          const polygon = new AMap.Polygon({
            path: bounds,
            strokeWeight: 3,
            strokeColor: "#00edfa",
            fillOpacity: 0.1,
          });
          polygon.setMap(map);
        }
      } else {
        console.error('获取贵州省边界失败:', result);
      }
    });
  }).catch((e) => {
    console.error("地图加载失败:", e);
  });
}

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

.fdq_container {
  background-image: url("https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/fdq_bg.png");
  background-size: cover;
  position: relative;
  z-index: 200;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 头部样式 */
.header-panel {
  background-image: url("https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/fdq_header.png");
  background-repeat: no-repeat;
  background-size: contain;
  background-position: center;
  width: 100%60;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  height: 60px;
  border-radius: 12px;
}

/* 主内容区样式 */
.fdq_main-panel {
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
  min-height: 500px;
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
  position: absolute;
  gap: 15px;
  border: 1px;
  font-weight: bold;
  width: 99.5%;
  background-color: rgba(0, 0, 0, 0.75);
  left: 0.25%;
  bottom: 200px;
  flex-wrap: wrap;
}

/* 新增容器样式 */
.new-container {
  position: absolute;
  width: 99.5%;
  margin: 0 auto;
  /* 假设左右两边容器最小高度为300px，新容器高度为一半 */
  min-height: 200px;
  bottom: 0;
  left: 0.25%;
  background: rgba(14, 38, 59, 0.9);
  z-index: 201;
  //margin-top: 10px;
}

.new-container::before{
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  //height: 4px;
  //background:linear-gradient(90deg, rgba(14, 208, 242, 1)0%,rgba(255, 255, 255, 0) 100%);
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

.orange-bg {
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
  //border-radius: 12px;
  //padding: 10px;
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
  //height: 4px;
  //background:linear-gradient(90deg, rgba(14, 208, 242, 1)0%,rgba(255, 255, 255, 0) 100%);
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
  border-radius: 20px 0  0 20px;
  background:linear-gradient(90deg, rgba(14, 208, 242, 1)0%,rgba(255, 255, 255, 0) 100%);
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
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #66ffff;
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
