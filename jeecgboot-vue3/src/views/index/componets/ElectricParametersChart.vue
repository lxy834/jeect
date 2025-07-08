<template>
  <div class="dashboard-container">
    <div class="header-panel">
      <div style="width: 100%">
       <div class="icon-white" style="text-align: center;width: 100%;color:'#ffffff';">
          <span style="font-weight:bold;font-size:1.5rem;letter-spacing:1px">米箩变10kV米仲线仲盐联络线米仲-米坝TT10断路器FTU</span>
          <i style="font-size:2.5rem;float:right;margin-right:15px;  align-items: center;" class="fas fa-expand"  v-show="state.show" @click="exit(true)"/>
          <i style="font-size:2.5rem;float:right;margin-right:15px;  align-items: center;" class="fa fa-compress" v-show="!state.show" @click="exit(false)"/>

       </div>
</div>
    </div>
    <!-- 主内容区 -->
    <div class="main-panel" id="map">
      <div class="left-stats">
        <div class="stat-card">
          <div class="device-stats">
            <dv-border-box-9 :color="['#66FFFF', '#66FFFF']" style="height:100%;width:100%;min-height:300px;">
              <div class="card-title">
                <i class="	fa fa-bolt" />
                电压趋势
              </div>
              <div style="height:100%;width:98%;color:red;margin-left:2%">123</div>
            </dv-border-box-9>
          </div>
        </div>
        <div class="stat-card">
          <div class="event-stats">
            <dv-border-box-9 :color="['#66FFFF', '#66FFFF']" style="height:100%;width:100%;min-height:300px;">
              <div class="card-title">
                <i class="fas fa-microchip"></i>
                有功趋势
              </div>
              <div style="height:100%;width:100%;"></div>
            </dv-border-box-9>
          </div>
        </div>
      </div>
      <div class="center-stats">
        <!-- 信道信息 -->
        <div class="stat-card">
          <div class="event-stats">
            <dv-border-box-9 :color="['#66FFFF', '#66FFFF']" style="height:100%;width:100%;min-height:300px;">
              <div class="card-title">
                <i class="fas fa-bar-chart" />
                线路图
              </div>
              <div style="height:88%;width:95%;background-image:url('https://pic1.zhimg.com/0aacef55810d4949d28f3f7a1147aad0_r.jpg');background-size:contain;min-height:280px;margin-left:3%;margin-top:1%"></div>
            </dv-border-box-9>
          </div>
        </div>
        <div class="stat-card">
          <div class="event-stats">
            <dv-border-box-9 :color="['#66FFFF', '#66FFFF']" style="height:100%;width:100%;min-height:300px;">
              <div class="card-title">
                <i class="fa fa-pie-chart" />
                事件统计
              </div>
              <div style="height:100%;width:100%;"></div>
            </dv-border-box-9>
          </div>
        </div>
      </div>
      <!-- 右侧数据卡片 - 重新设计 -->
      <div class="right-stats">
        <!-- 控制功能 -->
        <div class="stat-card">
          <div class="event-stats">
            <dv-border-box-9 :color="['#66FFFF', '#66FFFF']" style="height:100%;width:100%;min-height:300px;">
              <div class="card-title">
                <i class="	fa fa-font"></i>
                电流趋势
              </div>
              <div style="height:100%;width:100%;"></div>
            </dv-border-box-9>
          </div>
        </div>
        <div class="stat-card">
          <div class="event-stats">
            <dv-border-box-9 :color="['#66FFFF', '#66FFFF']" style="height:100%;width:100%;min-height:300px;">
              <div class="card-title">
                <i class="fa fa-area-chart" />
                功率因素趋势
              </div>
              <div style="height:100%;width:100%;"></div>
            </dv-border-box-9>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import screenfull from "screenfull";
import { useAppStore } from "@/store/modules/app";
import { onMounted, reactive, ref, watchEffect } from "vue";
import { useMenuSetting } from "/@/hooks/setting/useMenuSetting";
import "@fortawesome/fontawesome-free/css/all.css";
import { onBeforeRouteLeave } from "vue-router";

const appStore = useAppStore();
const {  setMenuSetting } = useMenuSetting();

const state = reactive({
  show: true,
  dialogVisible: false,
  myChart: null,
  stat: null,
  ftuDeviceList: [],
  volumeList: [],
  bdCount: 0,
  dhCount: 0,
  hhCount: 0,
  warnList: []
});

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

  setMenuSetting({ show: true });
  appStore.setLayoutHideHeader(true);
  appStore.setLayoutHideMultiTabs(true);
  appStore.setLayoutHideSider(true);
  state.show = true;
});
</script>

<style lang="less" scoped>
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
  background-image: url("https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/data.png");
  background-repeat: no-repeat;
  background-size: 100% 100%;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  height: 72px;
  //border-radius: 12px;
}

/* 主内容区样式 */
.main-panel {
  flex-grow: 1;
  z-index: 100;
  display: flex;
  flex-wrap: wrap;
}

.center-stats {
  z-index: 100;
  display: flex;
  flex-direction: column;
  flex: 1;
  //min-width: 800px;
  margin: 0 10px 0 10px;
}

.left-stats,
.right-stats {
  z-index: 100;
  display: flex;
  flex-direction: column;
  flex: 1;
  //min-width: 400px;
  margin: 0 10px 0 10px;
}

.center-stats .stat-card,
.left-stats .stat-card,
.right-stats .stat-card {
  flex: 1;
  margin-top: 1%;
  min-height: 300px;
}

/* 重新设计的卡片样式 */
.stat-card {
  border-radius: 12px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgb(0, 119, 255);
}

/* 设备统计样式 */
.device-stats {
  display: flex;
  justify-content: space-between;
  height: 100%;
}

.qp {
  float: right;
  cursor: pointer;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  //margin: 15px auto;
  border-radius: 10px;
  font-size: 1.3rem;
}

.icon-white {
  background: rgba(47, 137, 252, 0);
  color: #ffffff;
}

/* 事件统计样式 */
.event-stats {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
}

.card-title {
  border-radius: 20px 0  0 20px;
  width:97%;
  background:linear-gradient(90deg, rgba(14, 208, 242, 1)0%,rgba(255, 255, 255, 0) 100%);
  display: flex;
  align-items: center;
  margin-top: 10px;
  margin-left: 15px;
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
}

.card-title i {
  margin-right: 10px;
  font-size: 1.5rem;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #66ffff;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .left-stats,
  .right-stats {
    min-width: 250px;
  }
}

@media (max-width: 992px) {
  .main-panel {
    flex-direction: column;
  }
}
</style>
