<template>
  <div class="dashboard-container">
    <div class="header-panel">
      <div style="text-align: right;width: 100%">
        <div class="qp icon-white">
          <i class="fas fa-expand"  v-show="state.show" @click="exit(true)"/>
          <i class="fa fa-compress" v-show="!state.show" @click="exit(false)"/>
          <!--          <i class="fas fa-compress-wide" />-->
        </div>
      </div>
    </div>
    <!-- 主内容区 -->
    <div class="main-panel" id="map">
      <div class="left-stats">

        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-exchange-alt"></i>
            馈线信息
          </div>
          <table class="real-time-table">
            <thead>
            <tr class="head">
              <th >终端名称</th>
              <th >馈线名称</th>
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
                <tr class="head">

                </tr>
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
                <div class="event-value">0</div>
              </div>
              <div class="event-item">
                <div class="stat-icon icon-orange">
                  <i class="fas fa-line-chart" />
                </div>

                <div class="event-label">FTU故障</div>
                <div class="event-value">0</div>
              </div>
              <div class="event-item">
                <div class="stat-icon icon-red">
                  <i class="fas fa-exclamation-triangle"></i>
                </div>
                <div class="event-label">掉线</div>
                <div class="event-value">0</div>

              </div>

              <div class="event-item">
                <div class="stat-icon icon-purple">
                  <i class="fas fa-sliders-h"></i>
                </div>
                <div class="event-label">其它</div>
                <div class="event-value">0</div>

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
              <tbody>

              </tbody>
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
import * as echarts from "echarts";
import { ftuF411DeviceList, list, volume } from "@/views/ftu/list/FtuDevice.api.ts";
import { defHttp } from "@/utils/http/axios";
import { getWarnList } from "@/views/ftu/warn/FtuWarnInfo.api";
import { getStat } from "@/views/ftu/f411/FtuF411Device.api.ts";
import { useGo } from '/@/hooks/web/usePage';
const appStore = useAppStore();
const {  setMenuSetting } = useMenuSetting();
window._AMapSecurityConfig = {
  securityJsCode: "d1243371803f635fdfa7b253ffb723e0" // 安全密钥
};

const feederTableContainer = ref(null);
const realTimeTableContainer = ref(null);

const state =reactive({
  show:true,
  dialogVisible:false,
  myChart:null,
  stat:null,
  ftuDeviceList:[],
  volumeList:[],
  bdCount:0,
  dhCount:0,
  hhCount:0,
  warnList:[]
})

async function getList() {
  let form = {
    order: 'desc',
    pageNo: 1,
    pageSize: 10,
  }
  await list(form).then(res =>{
    state.ftuDeviceList = res.records
    initMap()
  })
  state.volumeList = await defHttp.get({ url: volume })

  let warn = {
    order: 'desc',
    pageNo: 1,
    pageSize: 10,
  }
  await getWarnList(warn).then(res =>{
    state.warnList = res.records
  })

  const data = await defHttp.get({url: getStat});
  for (let i = 0; i < data.length; i++) {
    switch (data[i].COMMUNICATION_MODE){
      case 0:
        state.bdCount=state.bdCount+1
        break;
      case 1:
        state.dhCount=state.dhCount+1
        break
      case 2:
        state.hhCount=state.hhCount+1
        break;
    }
  }
}

function initMap(){
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
      mapStyle: "amap://styles/d86da4c2ed42be8272eb068059df8719" // 使用清新灰色风格地图
    });
    // 添加模拟标记点
    let positions = [];
    for (let i = 0; i < state.ftuDeviceList.length; i++) {

      const position = {
        position:[state.ftuDeviceList[i].lng,state.ftuDeviceList[i].lat],
        deviceName:state.ftuDeviceList[i].deviceName,
        insLineName:state.ftuDeviceList[i].insLineName,
        id:state.ftuDeviceList[i].id
      }
      switch (state.ftuDeviceList[i].status){
        case 0:
          position.status = "dh_active"
          position.commStatus = "北斗在线"
          break
        case 1:
          position.status = "bd_active"
          position.commStatus = "电鸿在线"
          break
        case 2:
          position.status = "f411_offline"
          position.commStatus = "一体机离线"
          break;
        case 3:
          position.status = "ftu_warning"
          position.commStatus = "电鸿在线"
          break;
        case 4:
          position.status = "error"
          position.commStatus = "电鸿在线"
          break
        case 5:
          position.status = "hh_comm"
          position.commStatus = "混合模式"
          break
      }
      positions.push(position)
    }
    let infoWindow
    // 根据状态添加不同颜色的标记
    positions.forEach(point => {
      const color =
        point.status === "bd_active" ? "#41C23C" :
        point.status === "dh_active" ? "#2F89FC" :
        point.status === "hh_comm" ? "#6633ff" :
        point.status === "f411_offline" ? "#9BA3A9" :
          point.status === "ftu_warning" ? "#FFC600" :
            point.status === "error" ? "#FF3636" : "#95A5A6";

      let marker = new AMap.Marker({
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
        offset: new AMap.Pixel(-10, -20)
      });

      marker.setLabel({
        direction: "bottom-center",
        offset: new AMap.Pixel(0, 0),  //设置文本标注偏移量
        // content: `<div style="background: ${color};font-size: 16px">${point.title}</div>` //设置文本标注内容
      });

      marker.on("click",async (e) => {
        let params = {
          id:point.id
        }
        const data = await defHttp.get({url: ftuF411DeviceList,params});
        var info = [];
        info.push(`<div style="background: black"><div style="background-image: url('https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/17.png');height: 250px;width: 500px;background-repeat: no-repeat;background-size: cover">
      <div style="width: 100%;text-align: right;color: white;font-weight: bold">${point.deviceName}</div>
      <div style="height: 84%;width: 100%;display: flex">
        <div style="width: 60%;height: 100%;color: rgba(99, 242, 255, 1)">
          <div style="height: 24%;margin-top: 4%;margin-left: 2%">设备名称：${point.deviceName}</div>
          <div style="height: 18%;margin-top: 2%;margin-left: 2%">线路名称：${point.insLineName}</div>
          <div style="height: 18%;margin-top: 2%;margin-left: 2%">设备编码：${data[0].deviceCode}</div>
          <div style="height: 18%;margin-top: 2%;margin-left: 2%">通信状态：${point.commStatus}</div>
          <div style="height: 12%;margin-top: 2%;margin-left: 2%">历史数据：<span  class="info-content" style="cursor: pointer;text-decoration: underline;color: white;font-weight: bold">查看</span></div>
        </div>
        <div style="width: 40%;height: 96%;margin-top:4%">
          <img src='https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/ftu.jpg' style='width: 100%;height:100%'/>
        </div>
      </div>
    </div></div>`)
        infoWindow = new AMap.InfoWindow({
          offset: new AMap.Pixel(0, -15),
          content: info.join("")  //使用默认信息窗体框样式，显示信息内容
        });
        infoWindow.open(map, marker.getPosition());
        const infoWindowContent = document.querySelector('.info-content');
        if (infoWindowContent) {
          infoWindowContent.addEventListener('click', () => {
            goData(point);
          });
        }
      })

      map.on("click", (e) => {
        // 判断点击目标是否为 Marker
        if (!(e.target instanceof AMap.Marker)) {
          infoWindow.close();
        }
      });
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
function goData(v){

  go({
    path:'/index/componets',
    query:{
      ftu:v.id,
      device:v.deviceName
    }
  });

}

function exit(v){
  if(v){
    state.show = false
    screenfull.request();
    setMenuSetting({ show: false });
    appStore.setLayoutHideHeader(true);
    appStore.setLayoutHideSider(true);
    appStore.setLayoutHideMultiTabs(true);
  }else{
    state.show = true

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
        state.show = false
        // 全屏时隐藏菜单
        setMenuSetting({ show: false });
      } else {
        state.show = true
        // 退出全屏时显示菜单
        setMenuSetting({ show: true });
        appStore.setLayoutHideHeader(false);
        appStore.setLayoutHideMultiTabs(false);
        appStore.setLayoutHideSider(false);
      }
    });
  }
});
onBeforeRouteLeave(()=>{
  appStore.setLayoutHideHeader(false);
  setMenuSetting({ show: true });
  appStore.setLayoutHideMultiTabs(false);
  appStore.setLayoutHideSider(false);
})

const startAutoScroll = (container) => {
  if (!container) return;
  const table = container.querySelector('table');
  const rows = table.querySelectorAll('tbody tr');
  if (rows.length <= 5) return; // 当行数小于等于5时，不滚动
  const rowHeight = rows[0].offsetHeight;
  let scrollTop = 0;
  const intervalId = setInterval(() => {
    scrollTop += rowHeight;
    if (scrollTop >= table.offsetHeight - container.offsetHeight) {
      scrollTop = 0;
    }
    container.scrollTop = scrollTop;
  }, 3000); // 每3秒滚动一次
  return intervalId;
};

onMounted(() => {
  document.addEventListener('keydown', function(event) {
    if (event.keyCode === 27) { // 27是ESC键的keyCode
      setMenuSetting({ show: true });
      appStore.setLayoutHideHeader(false);
      appStore.setLayoutHideMultiTabs(false);
      appStore.setLayoutHideSider(false);
      state.show = true
    }
  });
  getList()
  // 实现表格自动轮播
  if (feederTableContainer.value) {
    startAutoScroll(feederTableContainer.value);
  }
  if (realTimeTableContainer.value) {
    startAutoScroll(realTimeTableContainer.value);
  }
  setMenuSetting({ show: true });
  appStore.setLayoutHideHeader(true);
  appStore.setLayoutHideMultiTabs(true);
  appStore.setLayoutHideSider(true);
  state.show = true

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
  //background: rgb(3,52,71);
  background-image: url("https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/bg_ftu.png");
  background-size: cover;
  position: relative;
  z-index: 200;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  //padding: 10px;
  gap: 5px;
}

/* 头部样式 */
.header-panel {
  background-image: url("https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/xtmc.png");
  background-repeat: no-repeat;
  background-size: contain; /* 修改为contain，确保背景图片适应容器 */
  background-position: center; /* 背景图片居中显示 */
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  height: 60px;
  //background: rgba(10, 20, 40, 0.5);
  border-radius: 12px;
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
  flex-grow: 1; /* 占满剩余高度 */
  z-index: 100;
  display: flex;
  flex-wrap: wrap;
  //gap: var(--panel-spacing);
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
  flex: 1; /* 每个模块占main-panel高度的33% */
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
  //box-shadow: 0 0 20px rgba(0, 0, 0, 0.3);
}

.map-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
}

.map-container-title {
  //background-color: rgba(0, 0, 0, 0.2);
  z-index: 100;
  //background: rgba(25, 35, 51, 0.8);
  font-size: 2rem;
  color: white;
  font-weight: bold;
  text-align: center;
  letter-spacing: 1px;
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
  color: #41C23C;
}

.status-warning {
  color: #FFC600;
}

.status-error {
  color: #FF3636;
}

.status-info {
  color: #2F89FC;
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
  //color: #9BA3A9;
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

/* 故障信息区 */
.fault-panel {
  display: flex;
  flex-wrap: wrap;
  gap: var(--panel-spacing);
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
  width: 30%;
  color: white;
  background: rgba(15, 26, 37, 0.7);
  border: 1px solid rgba(30, 145, 255, 0.3);
  border-radius: 12px;
  padding: 10px;
  position: relative;
  max-height: 200px;
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
  width: 20px;
  height: 20px;
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
  text-align: left;
  padding: 10px 15px;
  color: white;
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
  background: rgba(65, 194, 60, 0.7);
}

.bg-blue {
  background: rgba(47, 137, 252, 0.7);
}

.bg-gray {
  background: rgba(155, 163, 169, 0.7);
}

.bg-red {
  background: rgba(255, 54, 54, 0.7);
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
    height: 240px;
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

::v-deep .amap-info-content{
  padding: 0;
}

::v-deep .el-dialog__header{
  background: rgba(14, 38, 59, 0.9);
}

::v-deep .el-dialog{
  background: rgba(14, 38, 59, 0.9);
}

::v-deep .amap-info-close{
  display: none;
}

</style>
