<template>
  <div class="dashboard-container">

    <!-- 悬浮按钮容器 -->
    <div
      class="floating-button-container"
      ref="floatingContainer"
      @mousedown="startDrag"
      style="position: fixed;  z-index: 9999; cursor: move;"
    >
      <!-- 主悬浮按钮 -->
      <button
        class="floating-button"
        @click="toggleMenu"
        @mouseenter="showTooltip('mainTooltip')"
        @mouseleave="hideTooltip('mainTooltip')"
        style="cursor: pointer;"
      >
        <i class="fas" :class="state.isOpen ? 'fa-times' : 'fa-plus'"></i>
      </button>
      <div
        class="tooltip"
        :class="{ 'tooltip-visible': state.tooltips.mainTooltip }"
        ref="mainTooltip"
      >
        快捷导航
      </div>
      <!-- 展开的子按钮 -->
      <div
        class="floating-menu"
        :class="{ 'menu-open': state.isOpen }"
      >
        <!-- 第一个子按钮 -->
        <div class="tooltip-container">
          <button
            class="floating-menu-item"
            @mouseenter="showTooltip('listTooltip')"
            @mouseleave="hideTooltip('listTooltip')"
            @click="state.dialog = false,state.dialogVisible = true,state.title='馈线负荷一览表'"
          >
            <i class="fa fa-line-chart" />
          </button>
          <div
            class="tooltip"
            :class="{ 'tooltip-visible': state.tooltips.listTooltip }"
            ref="listTooltip"
          >
            馈线负荷一览表
          </div>
        </div>

        <!-- 第二个子按钮 -->
        <div class="tooltip-container">
          <button
            class="floating-menu-item"
            @mouseenter="showTooltip('fontTooltip')"
            @mouseleave="hideTooltip('fontTooltip')"
            @click="state.dialogVisible = false,state.dialog = true,state.title='FTU在线一览表'"
          >
            <i class="fas fa-clipboard-list"></i>
          </button>
          <div
            class="tooltip"
            :class="{ 'tooltip-visible': state.tooltips.fontTooltip }"
            ref="fontTooltip"
          >
            FTU在线一览表
          </div>
        </div>
      </div>
    </div>

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

          <div class="status-item">
            <span class="status-dot overload-small"></span>
            <span>电流负荷小</span>
          </div>

          <div class="status-item">
            <span class="status-dot overload-big"></span>
            <span>电流负荷大</span>
          </div>

          <div class="status-item">
            <span class="status-dot overload"></span>
            <span>负荷过载</span>
          </div>

          <div class="status-item">
            <span class="status-dot small"></span>
            <span>通信质量差</span>
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
            运行指标
          </div>
          <div class="control-grid">
            <div class="control-item">
              <div style="color: #FFFFFF;font-size: 1rem">通信成功率</div>
              <el-progress
                :percentage="85"
                :stroke-width="8"
                style="flex: 1;margin-left: 10px"
              />
            </div>
          </div>
          <div class="control-grid">
            <div class="control-item">
              <div style="color: #FFFFFF;font-size: 1rem">遥信成功率</div>
              <el-progress
                :percentage="85"
                :stroke-width="8"
                style="flex: 1;margin-left: 10px"
              />
            </div>
          </div>
          <div class="control-grid">
            <div class="control-item" >
              <div style="color: #FFFFFF;font-size: 1rem">遥测成功率</div>
              <el-progress
                :percentage="85"
                :stroke-width="8"
                style="flex: 1;margin-left: 10px"
              />
            </div>
          </div>
          <div class="control-grid">
            <div class="control-item">
              <div style="color: #FFFFFF;font-size: 1rem">遥控成功率</div>
              <el-progress
                :percentage="85"
                :stroke-width="8"
                style="flex: 1;margin-left: 10px"
              />
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
    </div>
    <el-dialog
      v-model="state.dialogVisible"
      :title="state.title"
      draggable
      width="65%"
      max-height="80vh"
    >
      <el-table :header-cell-style="{background:'rgba(14, 38, 59, 1)',color:'#66ffff'}"
                :cell-style="{background:'rgba(14, 38, 59, 0.95)',color:'#ffffff'}" :data="state.tableData"  stripe style="width: 100%;border: 1px solid #66ffff">
        <el-table-column prop="deviceName"  label="名称" align="center"/>
        <el-table-column prop="maxCurrent" label="允许电流" align="center"/>
        <el-table-column prop="dayMax"  label="当日最大" align="center"/>
        <el-table-column prop="nowCurrent" label="实时电流" align="center"/>
        <el-table-column label="负载率" align="center">
          <template #default="scope">
            <div v-if="scope.row.maxCurrent > 0">
              <div class="flex items-center gap-2">
                <!-- 进度条 -->
                <el-progress
                  :percentage="loadRate(scope.row)"
                  :stroke-width="8"
                  :color="loadRateColor(scope.row)"
                  style="flex: 1"
                />
              </div>
            </div>
            <div v-else>--</div>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top: 5px" size="small" layout="->,prev, pager, next" :total="state.total" :background="true"/>
    </el-dialog>

    <el-dialog
      v-model="state.dialog"
      :title="state.title"
      draggable
      width="65%"
      max-height="80vh"
    >
      <el-table :header-cell-style="{background:'rgba(14, 38, 59, 1)',color:'#66ffff'}" height="500"
                :cell-style="{background:'rgba(14, 38, 59, 0.95)',color:'#ffffff'}" :data="state.ftuOnlineList"  stripe style="width: 100%;border: 1px solid #66ffff">
        <el-table-column prop="station" label="站名" align="center"/>
        <el-table-column prop="insLineName"  label="线路名" align="center"/>
        <el-table-column prop="deviceName" label="名称" align="center" show-overflow-tooltip/>
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <!-- 状态为0时显示绿色闪烁圆圈 -->
            <div v-if="scope.row.onlineStatus === 0" class="status-circle green-blink"></div>
            <!-- 状态为1时显示红色闪烁圆圈 -->
            <div v-if="scope.row.onlineStatus === 1" class="status-circle red-blink"></div>
          </template>
        </el-table-column>
        <el-table-column prop="ip"  label="IP地址" align="center"/>
        <el-table-column prop="commStatus"  label="通信模式" align="center"/>
      </el-table>
      <el-pagination style="margin-top: 5px" size="small" layout="->,prev, pager, next" :total="state.total" :background="true"/>
    </el-dialog>
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
import { ftuF411DeviceList, getIndexList, volume } from "@/views/ftu/list/FtuDevice.api.ts";
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
  dialog:false,
  ftuDeviceList: [],
  ftuOnlineList:[],
  volumeList: [],
  bdCount: 0,
  dhCount: 0,
  hhCount: 0,
  warnList: [],
  statForm: {},
  tableData:[{
    deviceName:"梭嘎变10kV湾河Ⅰ回线",
    maxCurrent:400,
    dayMax:381,
    nowCurrent:297,
  },{
    deviceName:"梭嘎变10kV湾河Ⅰ回线",
    maxCurrent:400,
    dayMax:381,
    nowCurrent:30,
  },{
    deviceName:"梭嘎变10kV湾河Ⅰ回线",
    maxCurrent:400,
    dayMax:381,
    nowCurrent:440,
  },{
    deviceName:"梭嘎变10kV湾河Ⅰ回线",
    maxCurrent:400,
    dayMax:381,
    nowCurrent:350,
  }],
  over:0,
  total:0,
  isOpen:false,
  title:"",
  tooltips: {
    mainTooltip: false,
    listTooltip: false,
    fontTooltip: false,
    helpTooltip: false
  }
});

const floatingContainer = ref(null);
const dragState = reactive({
  isDragging: false,
  startX: 0,
  startY: 0,
  initialX: 0,
  initialY: 0,
  hasMoved: false  // 用于判断是否发生了拖动
});

const startDrag = (e) => {
  // 只有点击主容器或按钮时才允许拖动
  if (e.target.closest('.floating-button-container') || e.target.closest('.floating-button')) {
    dragState.isDragging = true;
    dragState.hasMoved = false;
    dragState.startX = e.clientX;
    dragState.startY = e.clientY;

    const rect = floatingContainer.value.getBoundingClientRect();
    dragState.initialX = rect.left;
    dragState.initialY = rect.top;

    // 绑定到document上防止快速移动时断触
    document.addEventListener('mousemove', onDrag);
    document.addEventListener('mouseup', stopDrag);
    document.addEventListener('mouseleave', stopDrag);

    e.preventDefault();
  }
};


const onDrag = (e) => {
  if (!dragState.isDragging) return;

  // 标记已经发生移动
  dragState.hasMoved = true;

  const dx = e.clientX - dragState.startX;
  const dy = e.clientY - dragState.startY;

  const newX = dragState.initialX + dx;
  const newY = dragState.initialY + dy;

  // 限制在视窗内
  const windowWidth = window.innerWidth;
  const windowHeight = window.innerHeight;
  const containerWidth = floatingContainer.value.offsetWidth;
  const containerHeight = floatingContainer.value.offsetHeight;

  // 计算边界，增加一点缓冲
  const boundedX = Math.max(10, Math.min(newX, windowWidth - containerWidth - 10));
  const boundedY = Math.max(10, Math.min(newY, windowHeight - containerHeight - 10));

  floatingContainer.value.style.left = `${boundedX}px`;
  floatingContainer.value.style.top = `${boundedY}px`;
  floatingContainer.value.style.right = 'auto';
  floatingContainer.value.style.bottom = 'auto';
};


const stopDrag = () => {
  if (dragState.isDragging) {
    dragState.isDragging = false;
    // 移除document上的事件监听
    document.removeEventListener('mousemove', onDrag);
    document.removeEventListener('mouseup', stopDrag);
    document.removeEventListener('mouseleave', stopDrag);
  }
};
const toggleMenu = () => {
  // 只有当没有发生拖动时才执行切换菜单操作
  if (!dragState.hasMoved) {
    state.isOpen = !state.isOpen;
  }
};

// 计算负载率，返回数值用于计算颜色
function getLoadRateValue(row) {
  if (!row.nowCurrent || !row.maxCurrent) return 0;
  return (row.nowCurrent / row.maxCurrent) * 100;
}

// 计算负载率百分比，返回字符串用于显示
function loadRate(row) {
  return getLoadRateValue(row).toFixed(1);
}

// 基于当前行的负载率计算颜色
function loadRateColor(row){
  const rate = getLoadRateValue(row); // 使用当前行的实际负载率
  console.log('当前行负载率:', rate);
  if (rate > 100) return '#FF00FF';
  if (rate > 80) return '#00fbff';
  if (rate < 30) return '#87CEFA';
  return '#4CD964'; // 30-80之间的默认颜色
}
async function getList() {
  const pageParams = {
    order: 'desc',
    pageNo: 1,
    pageSize: 10
  };

  // 并行处理无依赖的请求
  const [deviceRes, volumeRes, warnRes, statRes, eventRes] = await Promise.all([

    defHttp.get({url:getIndexList}),
    defHttp.get({ url: volume }),
    getWarnList(pageParams),
    defHttp.get({ url: getStat }),
    defHttp.get({ url: eventStat }),

  ]);

  // 处理设备列表和初始化地图
  state.ftuDeviceList = deviceRes;
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
      state[countKey] = item.count;
    }
  });

  // 处理事件统计
  state.statForm = eventRes;
}

function initMap() {
  var cluster = []
  const statusMap = {
    0: { status: "bd_active", deviceStatus: "北斗在线", color: "#2F89FC" },
    1: { status: "dh_active", deviceStatus: "电鸿在线", color: "#41C23C" },
    2: { status: "f411_offline", deviceStatus: "一体机离线", color: "#9BA3A9" },
    3: { status: "ftu_warning", deviceStatus: "FTU故障", color: "#FFC600" },
    4: { status: "error", deviceStatus: "FTU告警", color: "#FF3636" },
    5: { status: "hh_comm", deviceStatus: "混合模式", color: "#6633ff" },
    6: { status: "overload-small", deviceStatus: "电流负荷小", color: "#87CEFA" },
    7: { status: "overload-big", deviceStatus: "电流负荷大", color: "#00fbff" },
    8: { status: "overload", deviceStatus: "负荷过载", color: "#FF00FF" },
    9: { status: "small", deviceStatus: "通信质量差", color: "#FF7D00" },
  };

  const commMap = {
    0: { communicationMode: 0, commStatus: "北斗在线"},
    1: { communicationMode: 1, commStatus: "电鸿在线"},
    2: { communicationMode: 2, commStatus: "混合模式"},
  }

  const style = document.createElement('style');
  style.textContent = `
    /* 电流负荷小 - 缓慢闪烁 */
    @keyframes smallLoadBlink {
      0% { opacity: 0.3; }
      50% { opacity: 1; }
      100% { opacity: 0.3; }
    }
    .small-load-blink {
      animation: smallLoadBlink 3s infinite;
    }

    /* 电流负荷大 - 中等速度闪烁 */
    @keyframes bigLoadBlink {
      0% { opacity: 0.2; }
      50% { opacity: 1; }
      100% { opacity: 0.2; }
    }
    .big-load-blink {
      animation: bigLoadBlink 2s infinite;
    }

    /* 负荷过载 - 快速闪烁（警告效果） */
    @keyframes overloadBlink {
      0% { opacity: 0; transform: scale(1); }
      50% { opacity: 1; transform: scale(1.1); }
      100% { opacity: 0; transform: scale(1); }
    }
    .overload-blink {
      animation: overloadBlink 1s infinite;
      box-shadow: 0 0 10px rgba(255, 0, 255, 0.8);
    }

    /* 通信质量差 - 不稳定闪烁 */
    @keyframes poorSignalBlink {
 0% { opacity: 0.2; }
      50% { opacity: 1; }
      100% { opacity: 0.2; }
    }
    .poor-signal-blink {
      animation: poorSignalBlink 2s infinite;
    }

    .blink-container {
      position: relative;
    }
  `;
  document.head.appendChild(style);

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
    const map = new AMap.Map("map", {
      center: [104.830389, 26.592528],
      zoom: 11,
      mapStyle: "amap://styles/d86da4c2ed42be8272eb068059df8719"
    });
    const markers = state.ftuDeviceList.map(device => {
      const statusInfo = statusMap[device.status] || statusMap[2]; // 默认使用离线状态
      const commStatus = commMap[device.communicationMode]; // 默认使用离线状态
      const status = device.status;
      // 判断设备状态类型
      const isSmallLoad = status === 6;
      const isBigLoad = status === 7;
      const isOverload = status === 8;
      const isPoorSignal = status === 9;

      const markerData = {
        position: [device.lng, device.lat],
        deviceName: device.deviceName,
        insLineName: device.insLineName,
        onlineStatus:device.onlineStatus,
        ip:device.ip,
        station:device.station,
        id: device.id,
        ...commStatus,
        ...statusInfo
      };

      state.ftuOnlineList.push(markerData)

      let markerContent;
      if (isSmallLoad) {
        markerContent = `
          <div class="small-load-blink" style="
            background: ${markerData.color};
            width: 20px;
            height: 20px;
            border: 2px solid white;
            border-radius: 50%;
            box-shadow: 0 0 5px rgba(0,0,0,0.3);
          "></div>
        `;
      } else if (isBigLoad) {
        markerContent = `
          <div class="big-load-blink" style="
            background: ${markerData.color};
            width: 20px;
            height: 20px;
            border: 2px solid white;
            border-radius: 50%;
            box-shadow: 0 0 5px rgba(0,0,0,0.3);
          "></div>
        `;
      } else if (isOverload) {
        markerContent = `
          <div class="overload-blink" style="
            background: ${markerData.color};
            width: 20px;
            height: 20px;
            border: 2px solid white;
            border-radius: 50%;
          "></div>
        `;
      } else if (isPoorSignal) {
        markerContent = `
          <div class="poor-signal-blink" style="
            background: ${markerData.color};
            width: 20px;
            height: 20px;
            border: 2px solid white;
            border-radius: 50%;
            box-shadow: 0 0 5px rgba(0,0,0,0.3);
          "></div>
        `;
      } else {
        markerContent = `
          <div style="
            background: ${markerData.color};
            width: 20px;
            height: 20px;
            border: 2px solid white;
            border-radius: 50%;
            box-shadow: 0 0 5px rgba(0,0,0,0.3);
          "></div>
        `;
      }

      const marker = new AMap.Marker({
        position: markerData.position,
        content: markerContent,
        offset: new AMap.Pixel(-10, -20)
      });
      marker.on("click", async () => {
        try {
          const data = await defHttp.get({url: ftuF411DeviceList,params: { id: markerData.id }});

          const infoWindowContent = `
            <div style="background: black">
              <div style="background-image: url('https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/18.png');height: 250px;width: 500px;background-repeat: no-repeat;background-size: cover">
                <div style="width: 100%;text-align: right;color: white;font-weight: bold">${markerData.commStatus}--${markerData.deviceName}</div>
                <div style="height: 84%;width: 100%;display: flex">
                  <div style="width: 60%;height: 100%;color: #ffffff">
                    <div style="height: 24%;margin-top: 4%;margin-left: 2%">设备名称：${markerData.deviceName}</div>
                    <div style="height: 18%;margin-top: 2%;margin-left: 2%">线路名称：${markerData.insLineName}</div>
                    <div style="height: 18%;margin-top: 2%;margin-left: 2%">设备编码：${data[0]?.deviceCode || '未知'}</div>
                    <div style="height: 18%;margin-top: 2%;margin-left: 2%">当前状态：${markerData.deviceStatus}</div>
                    <div style="margin-top: 2%;margin-left: 2%">历史数据：<span class="info-content" style="cursor: pointer;text-decoration: underline;color: skyblue;font-weight: bold">查看</span></div>
                  </div>
                  <div style="width: 40%;height: 96%;margin-top:4%">
                    <img src='https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/ftu.jpg' style='width: 100%;height:100%'/>
                  </div>
                </div>
              </div>
            </div>
          `;

          let infoWindow = marker.infoWindow;
          if (!infoWindow) {
            infoWindow = new AMap.InfoWindow({
              offset: new AMap.Pixel(0, -15),
              content: infoWindowContent
            });
            marker.infoWindow = infoWindow;
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
    map.add(markers);

    map.setFitView(markers)
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

function showTooltip(tooltipName) {
  state.tooltips[tooltipName] = true;
}

function hideTooltip(tooltipName) {
  state.tooltips[tooltipName] = false;
}

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
  min-height: 500px;
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
  position: absolute;
  gap: 15px;
  border: 1px;
  font-weight: bold;
  background-color: rgba(0, 0, 0, 0.75);
  bottom: 0;
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

.small{
  background: #FF7D00;
}

.overload{
  background: #FF00FF;
}

.overload-small{
  background: #87CEFA ;
}

.overload-big{
  background: #00fbff;
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
  background:linear-gradient(90deg, rgba(14, 208, 242, 1)0%,rgba(255, 255, 255, 0) 100%);
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
  color: red;
  //display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.control-item {
  display: flex;
  background: rgba(20, 33, 51, 0.3);
  border-radius: 10px;
  padding: 20px;
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

::v-deep .el-dialog__title{
  color: #66ffff;
}

::v-deep .el-dialog__headerbtn{
  display: none;
}

::v-deep .el-dialog{
  border: 2px solid transparent;
  border-top-color: #66ffff;
  background: rgba(14, 38, 59, 0.9);
}

::v-deep .amap-info-close {
  display: none;
}

/* 悬浮按钮容器 - 固定在右侧中间 */
.floating-button-container {
  position: fixed;
  right: 20px;
  top: calc(50% + 30px);
  transform: translateY(-50%);
  z-index: 9999;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

/* 提示框容器 */
.tooltip-container {
  position: relative;
  display: flex;
  justify-content: center;
}

/* 主悬浮按钮样式 - 暗黑色风格 */
.floating-button {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: #1a1f2c;
  border: 2px solid #2d3748;
  color: #38bdf8;
  box-shadow: 0 4px 20px #38bdf8;
  font-size: 26px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.floating-button:hover {
  background-color: #2d3748;
  color: #7dd3fc;
  transform: scale(1.08);
  box-shadow: 0 6px 25px rgba(0, 0, 0, 0.4);
}

/* 子菜单容器 */
.floating-menu {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
  transform: translateX(20px);
}

/* 子菜单展开状态 */
.menu-open {
  opacity: 1;
  visibility: visible;
  transform: translateX(0);
}

/* 子按钮样式 */
.floating-menu-item {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: #1a1f2c;
  border: 1px solid #2d3748;
  color: #38bdf8;
  box-shadow: 0 3px 15px rgba(0, 0, 0, 0.9);
  font-size: 20px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.floating-menu-item:hover {
  background-color: #2d3748;
  color: #7dd3fc;
  transform: scale(1.05);
}

/* 提示框样式 */
.tooltip {
  position: absolute;
  right: 100%;
  white-space: nowrap;
  margin-right: 15px;
  margin-top: 15px;
  padding: 6px 12px;
  background-color: rgba(17, 24, 39, 0.9);
  color: #e2e8f0;
  border-radius: 6px;
  font-size: 14px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
  opacity: 0;
  visibility: hidden;
  transition: all 0.2s ease;
  z-index: 10000;
}

/* 提示框显示状态 */
.tooltip-visible {
  opacity: 1;
  visibility: visible;
}

/* 提示框小三角 */
.tooltip::after {
  content: "";
  position: absolute;
  top: 50%;
  left: 100%;
  margin-top: -5px;
  border-width: 5px;
  border-style: solid;
  border-color: transparent transparent transparent rgba(17, 24, 39, 0.9);
}

::v-deep .el-progress__text { // 修改进度条文字提示颜色
  color: #ffffff;
}

::v-deep .el-scrollbar__wrap{
  background:rgba(14, 38, 59, 0.95)
}

::v-deep.el-table th {
  border: 1px solid #66ffff !important;
  border-right: none !important;
  //border-left: none !important;
  border-bottom: none !important;
}

::v-deep.el-table td {
  border: 1px solid #66ffff !important;
  border-right: none !important;
  //border-left: none !important;
}

.status-circle {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  margin: 0 auto;
}

/* 绿色闪烁动画 */
.green-blink {
  background-color: #00ff00;
  animation: blink 1.5s infinite;
}

/* 红色闪烁动画 */
.red-blink {
  background-color: #ff0000;
  animation: blink 1.5s infinite;
}

/* 闪烁动画定义 */
@keyframes blink {
  0% { opacity: 1; }
  50% { opacity: 0.3; }
  100% { opacity: 1; }
}

</style>
