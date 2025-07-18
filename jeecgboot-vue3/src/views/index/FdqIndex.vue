<template>
  <div class="fdq_container">
    <div class="header-panel"></div>
    <div class="fdq_main-panel" id="fdq_map">
      <div class="left-stats">
        <!-- 1. 任务清单 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-clipboard-list"></i>
            任务清单
          </div>
          <!-- 任务清单样式 -->
          <div
            class="task-list-container"
            id="taskListContainer"
            @mouseenter="stopAutoScroll"
            @mouseleave="startAutoScroll"
          >
            <div
              v-for="(task, index) in tasks"
              :key="task.id"
              :ref="(el) => taskRefs[index] = el"
              class="task-item"
              :class="{ 'active-task': activeTaskIndex === index }"
              @click="selectTask(index)"
            >
            <div class="task-vehicle">{{ task.vehicleInfo }}</div>
            <div class="task-name">{{ task.name }}</div>
            <div class="task-user">{{ task.targetUser }}</div>
          </div>
        </div>
        </div>

        <!-- 2. 任务详情 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-microchip"></i>
            任务详情
          </div>
          <div class="task-steps">
            <el-steps
              direction="vertical"
              :active="currentStep"
              class="vertical-steps"
            >
              <el-step title="工单下发" :description="steps[0].desc" :status="steps[0].status"></el-step>
              <el-step title="前往现场" :description="steps[1].desc" :status="steps[1].status"></el-step>
              <el-step title="开始发电" :description="steps[2].desc" :status="steps[2].status"></el-step>
              <el-step title="归还设备" :description="steps[3].desc" :status="steps[3].status"></el-step>
            </el-steps>
          </div>
        </div>

        <!-- 3. 实时发电 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fa fa-bolt" />
            实时发电
          </div>
          <div class="realtime-grid">
            <div class="realtime-item">
              <div class="realtime-label">A相电压</div>
              <div class="realtime-value">{{ realtimeData.voltageA }}V</div>
            </div>
            <div class="realtime-item">
              <div class="realtime-label">B相电压</div>
              <div class="realtime-value">{{ realtimeData.voltageB }}V</div>
            </div>
            <div class="realtime-item">
              <div class="realtime-label">C相电压</div>
              <div class="realtime-value">{{ realtimeData.voltageC }}V</div>
            </div>
            <div class="realtime-item">
              <div class="realtime-label">A相电流</div>
              <div class="realtime-value">{{ realtimeData.currentA }}A</div>
            </div>
            <div class="realtime-item">
              <div class="realtime-label">B相电流</div>
              <div class="realtime-value">{{ realtimeData.currentB }}A</div>
            </div>
            <div class="realtime-item">
              <div class="realtime-label">C相电流</div>
              <div class="realtime-value">{{ realtimeData.currentC }}A</div>
            </div>
            <div class="realtime-item">
              <div class="realtime-label">频率转速</div>
              <div class="realtime-value">{{ realtimeData.power }}</div>
            </div>
            <div class="realtime-item">
              <div class="realtime-label">频率</div>
              <div class="realtime-value">{{ realtimeData.frequency }}Hz</div>
            </div>
            <div class="realtime-item">
              <div class="realtime-label">温度</div>
              <div class="realtime-value">{{ realtimeData.temperature }}°C</div>
            </div>
            <div class="realtime-item">
              <div class="realtime-label">电池电压</div>
              <div class="realtime-value">{{ realtimeData.batteryVoltage }}V</div>
            </div>
          </div>
        </div>
      </div>
      <div class="map-container">
        <div class="new-container">
          <div style="display: flex;justify-content: center;gap: 15px;">
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
          <div class="card-title">
            <i class="fas fa-exclamation-triangle"></i>
            告警信息
          </div>
          <div class="alarm-list">
            <div class="alarm-wrapper">
              <vue3ScrollSeamless
                style="margin: 0 auto; overflow: auto"
                :hover="true"
                :wheel="true"
                :isWatch="true"
                :classOptions="classOptions"
                :dataList="alarms"
              >
                <table class="real-time-table">
                  <thead>
                  <tr class="head"></tr>
                  <tr v-for="(item,i) of alarms" :key="i">
                    <td class="alarm-time">{{item.time}}</td>
                    <td class="alarm-content">{{item.content}}</td>
                  </tr>
                  </thead>
                </table>
              </vue3ScrollSeamless>
            </div>
          </div>
        </div>
      </div>
      <div class="right-stats">
        <!-- 4. 维护信息 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-clipboard-list"></i>
            维护信息
          </div>
          <div class="maintenance-grid">
            <div class="maintenance-item">
              <div class="maintenance-value">{{ maintenanceData.totalPower }}</div>
              <div class="maintenance-label">总发电量(kWh)</div>
            </div>
            <div class="maintenance-item">
              <div class="maintenance-value">{{ maintenanceData.remainingTime }}</div>
              <div class="maintenance-label">剩余维护时间(天)</div>
            </div>
            <div class="maintenance-item">
              <div class="maintenance-value">{{ maintenanceData.mileage }}</div>
              <div class="maintenance-label">运行里程(km)</div>
            </div>
            <div class="maintenance-item">
              <div class="maintenance-value">{{ maintenanceData.yearlyTasks }}</div>
              <div class="maintenance-label">年度作业次数</div>
            </div>
            <div class="maintenance-item">
              <div class="maintenance-value">{{ maintenanceData.maintenanceCount }}</div>
              <div class="maintenance-label">维护次数</div>
            </div>
            <div class="maintenance-item">
              <div class="maintenance-value">{{ maintenanceData.startupCount }}</div>
              <div class="maintenance-label">启动次数</div>
            </div>
          </div>
        </div>

        <!-- 5. 发电统计 -->
        <div class="stat-card" style="max-height: 500px">
          <div class="card-title">
            <i class="fas fa-sliders-h"></i>
            发电统计
          </div>
          <div class="statistics-grid">
            <div class="statistic-item" style="width: 50%;">
              <div class="statistic-icon">
                <i class="fas fa-gas-pump"></i>
              </div>
              <div class="statistic-value">{{ statisticsData.totalFuel }}</div>
              <div class="statistic-label">总油耗(L)</div>
            </div>
            <div class="statistic-item" style="width: 50%">
              <div class="statistic-icon">
                <i class="fas fa-fire"></i>
              </div>
              <div class="statistic-value">{{ statisticsData.fuelPerKwh }}</div>
              <div class="statistic-label">平均每kWh油耗(L)</div>
            </div>
          </div>
          <div class="statistics-grid" style="display: flex">
            <div class="statistic-item" style="width: 50%">
              <div class="statistic-icon">
                <i class="fas fa-clock" />
              </div>
              <div class="statistic-value">{{ statisticsData.countHours }}</div>
              <div class="statistic-label">发电总时长(H)</div>
            </div>
            <div class="statistic-item" style="width: 50%">
              <div class="statistic-icon">
                <i class="fas fa-yen-sign" />
              </div>
              <div class="statistic-value">{{ statisticsData.totalRevenue }}</div>
              <div class="statistic-label">发电总收益(元)</div>
            </div>
          </div>
        </div>

        <!-- 6. 运行效益 -->
        <div class="stat-card">
          <div class="card-title">
            <i class="fas fa-sliders-h"></i>
            运行效益
          </div>
          <div class="charts-container">
            <div class="chart-wrapper">
              <div id="energyChart" class="echart-container"></div>
            </div>
            <div class="chart-wrapper">
              <div id="benefitChart" class="echart-container"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, onUnmounted, watch } from "vue";
import { useAppStore } from "@/store/modules/app";
import { useMenuSetting } from "@/hooks/setting/useMenuSetting";
import AMapLoader from "@amap/amap-jsapi-loader";
import { ElSteps, ElStep } from 'element-plus';
import * as echarts from 'echarts';
import { vue3ScrollSeamless } from "vue3-scroll-seamless";
const appStore = useAppStore();
const { setMenuSetting } = useMenuSetting();
window._AMapSecurityConfig = {
  securityJsCode: "d1243371803f635fdfa7b253ffb723e0" // 安全密钥
};

// 任务数据
const tasks = ref([
  {
    id: 1,
    vehicleInfo: "发电车-贵A12345",
    name: "应急供电任务",
    targetUser: "贵阳市医院",
    step: 2,
    steps: [
      { status: 'success', desc: '2023-06-10 08:30' },
      { status: 'success', desc: '2023-06-10 09:15' },
      { status: 'process', desc: '进行中' },
      { status: 'wait', desc: '未开始' }
    ],
    realtimeData: {
      voltageA: 220.5,
      voltageB: 219.8,
      voltageC: 221.2,
      currentA: 153,
      currentB: 149,
      currentC: 151,
      power: 1499,
      frequency: 50.1,
      temperature: 42.3,
      batteryVoltage: 13.8
    }
  },
  {
    id: 2,
    vehicleInfo: "发电车-贵B67890",
    name: "常规供电保障",
    targetUser: "遵义会议中心",
    step: 1,
    steps: [
      { status: 'success', desc: '2023-06-10 07:00' },
      { status: 'process', desc: '进行中' },
      { status: 'wait', desc: '未开始' },
      { status: 'wait', desc: '未开始' }
    ],
    realtimeData: {
      voltageA: 0,
      voltageB: 0,
      voltageC: 0,
      currentA: 0,
      currentB: 0,
      currentC: 0,
      power: 0,
      frequency: 0,
      temperature: 35.1,
      batteryVoltage: 13.6
    }
  },
  {
    id: 3,
    vehicleInfo: "发电车-贵C54321",
    name: "紧急救援供电",
    targetUser: "六盘水灾区",
    step: 3,
    steps: [
      { status: 'success', desc: '2023-06-09 18:00' },
      { status: 'success', desc: '2023-06-09 20:30' },
      { status: 'success', desc: '2023-06-09 21:15' },
      { status: 'process', desc: '准备中' }
    ],
    realtimeData: {
      voltageA: 222.1,
      voltageB: 220.9,
      voltageC: 221.5,
      currentA: 21.4,
      currentB: 20.8,
      currentC: 21.2,
      power: 48.3,
      frequency: 49.9,
      temperature: 45.6,
      batteryVoltage: 13.9
    }
  },
  {
    id: 4,
    vehicleInfo: "发电车-贵C54321",
    name: "紧急救援供电",
    targetUser: "六盘水灾区",
    step: 3,
    steps: [
      { status: 'success', desc: '2023-06-09 18:00' },
      { status: 'success', desc: '2023-06-09 20:30' },
      { status: 'success', desc: '2023-06-09 21:15' },
      { status: 'process', desc: '准备中' }
    ],
    realtimeData: {
      voltageA: 222.1,
      voltageB: 220.9,
      voltageC: 221.5,
      currentA: 21.4,
      currentB: 20.8,
      currentC: 21.2,
      power: 48.3,
      frequency: 49.9,
      temperature: 45.6,
      batteryVoltage: 13.9
    }
  },
  {
    id: 5,
    vehicleInfo: "发电车-贵C54321",
    name: "紧急救援供电",
    targetUser: "六盘水灾区",
    step: 3,
    steps: [
      { status: 'success', desc: '2023-06-09 18:00' },
      { status: 'success', desc: '2023-06-09 20:30' },
      { status: 'success', desc: '2023-06-09 21:15' },
      { status: 'process', desc: '准备中' }
    ],
    realtimeData: {
      voltageA: 222.1,
      voltageB: 220.9,
      voltageC: 221.5,
      currentA: 21.4,
      currentB: 20.8,
      currentC: 21.2,
      power: 48.3,
      frequency: 49.9,
      temperature: 45.6,
      batteryVoltage: 13.9
    }
  }
]);

const taskRefs = ref<(HTMLElement | null)[]>([]);

let energyChart: echarts.ECharts, benefitChart: echarts.ECharts;
// 选中的任务索引
const activeTaskIndex = ref(0);
// 当前步骤
const currentStep = ref(tasks.value[0].step);
// 步骤详情
const steps = ref(tasks.value[0].steps);
// 实时发电数据
const realtimeData = ref(tasks.value[0].realtimeData);
// 任务自动滚动定时器
let taskInterval: NodeJS.Timeout | null = null;

// 切换任务
const selectTask = (index: number) => {
  activeTaskIndex.value = index;
  currentStep.value = tasks.value[index].step;
  steps.value = tasks.value[index].steps;
  realtimeData.value = tasks.value[index].realtimeData;

  // 滚动到选中项
  scrollToActiveTask(index);
};
const scrollToActiveTask = (index: number) => {
  const taskElement = taskRefs.value[index];
  const container = document.getElementById('taskListContainer');

  if (taskElement && container) {
    // 方案1: 滚动到视图中（保持在可见区域）
    taskElement.scrollIntoView({
      behavior: 'smooth', // 平滑滚动
      block: 'start'      // 顶部对齐
    });

  }
};
// 启动任务自动滚动
const startTaskAutoScroll = () => {
  taskInterval = setInterval(() => {
    // 计算下一个任务索引，循环切换
    const nextIndex = (activeTaskIndex.value + 1) % tasks.value.length;
    selectTask(nextIndex);
  }, 5000*12); // 5秒切换一次
};

// 维护信息数据
const maintenanceData = reactive({
  totalPower: 125800,
  remainingTime: 15,
  mileage: 38520,
  yearlyTasks: 127,
  maintenanceCount: 23,
  startupCount: 356
});

// 发电统计数据
const statisticsData = reactive({
  totalFuel: 38560,
  fuelPerKwh: 0.28,
  totalRevenue: 256800,
  countHours: 25945
});

// 告警信息
const alarms = ref([
  { id: 1, time: '2025/07/18 10:23', content: '贵A12345 温度偏高' },
  { id: 2, time: '2025/07/18 09:45', content: '贵B67890 电压波动' },
  { id: 3, time: '2025/07/18 08:12', content: '贵C54321 振动异常' },
  { id: 4, time: '2025/07/18 07:58', content: '贵A12345 未见工单作业' }
]);
const classOptions = {
  limitMoveNum: 5,
  step: 0.1
};

onMounted(() => {
  // 键盘事件监听
  document.addEventListener('keydown', function(event) {
    if (event.keyCode === 27) { // ESC键
      setMenuSetting({ show: true });
      appStore.setLayoutHideHeader(false);
      appStore.setLayoutHideMultiTabs(false);
      appStore.setLayoutHideSider(false);
    }
  });

  // 初始化布局
  setMenuSetting({ show: true });
  appStore.setLayoutHideHeader(true);
  appStore.setLayoutHideMultiTabs(true);
  appStore.setLayoutHideSider(true);

  // 初始化地图
  initMap();

  // 初始化图表
  initCharts();

  // 启动任务自动滚动
  startTaskAutoScroll();
});

// 组件卸载时清除定时器
onUnmounted(() => {
  if (taskInterval) {
    clearInterval(taskInterval);
  }
});

// 初始化地图
function initMap() {
  AMapLoader.load({
    key: "e28af433d6fabd84d33509eca1a3efa3",
    version: "2.0",
    plugins: ["AMap.DistrictSearch"]
  }).then((AMap) => {
    let bounds;
    const districtSearch = new AMap.DistrictSearch({
      subdistrict: 0,
      extensions: "all",
      level: "province"
    });

    let mask = [];
    districtSearch.search('贵州省', function(status, result) {
      if (status === 'complete' && result.districtList.length) {
        bounds = result.districtList[0].boundaries;
        if (bounds) {
          for (let i = 0; i < bounds.length; i++) {
            mask.push([bounds[i]]);
          }
          const map = new AMap.Map("fdq_map", {
            mask: mask,
            center: [106.6172, 26.5783],
            zoom: 7.5,
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

// 初始化图表
function initCharts() {
  // 能源利用同比环比图（柱状图）
  const energyDom = document.getElementById('energyChart') as HTMLDivElement;
  energyChart = echarts.init(energyDom);

  const energyOption = {
    title: {
      text: '能源利用效率',
      left: 'center',
      textStyle: {
        color: 'rgba(255, 255, 255, 0.9)'
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['同比', '环比'],
      textStyle: {
        color: 'rgba(255, 255, 255, 0.9)'
      },
      top: 30
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月'],
      axisLine: {
        lineStyle: {
          color: 'rgba(255, 255, 255, 0.3)'
        }
      },
      axisLabel: {
        color: 'rgba(255, 255, 255, 0.7)'
      },
      splitLine: {
        lineStyle: {
          color: 'rgba(255, 255, 255, 0.1)'
        }
      }
    },
    yAxis: {
      type: 'value',
      axisLine: {
        lineStyle: {
          color: 'rgba(255, 255, 255, 0.3)'
        }
      },
      axisLabel: {
        color: 'rgba(255, 255, 255, 0.7)'
      },
      splitLine: {
        lineStyle: {
          color: 'rgba(255, 255, 255, 0.1)'
        }
      }
    },
    series: [
      {
        name: '同比',
        type: 'bar',
        data: [65, 72, 68, 81, 76, 85],
        itemStyle: {
          color: 'rgba(47, 137, 252, 0.6)',
          borderColor: 'rgba(47, 137, 252, 1)',
          borderWidth: 1
        }
      },
      {
        name: '环比',
        type: 'bar',
        data: [58, 65, 70, 75, 82, 88],
        itemStyle: {
          color: 'rgba(65, 194, 60, 0.6)',
          borderColor: 'rgba(65, 194, 60, 1)',
          borderWidth: 1
        }
      }
    ]
  };

  energyChart.setOption(energyOption);

  // 发电效益同比环比图（折线图）
  const benefitDom = document.getElementById('benefitChart') as HTMLDivElement;
  benefitChart = echarts.init(benefitDom);

  const benefitOption = {
    title: {
      text: '发电效益(千元)',
      left: 'center',
      textStyle: {
        color: 'rgba(255, 255, 255, 0.9)'
      }
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['同比', '环比'],
      textStyle: {
        color: 'rgba(255, 255, 255, 0.9)'
      },
      top: 30
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['1月', '2月', '3月', '4月', '5月', '6月'],
      axisLine: {
        lineStyle: {
          color: 'rgba(255, 255, 255, 0.3)'
        }
      },
      axisLabel: {
        color: 'rgba(255, 255, 255, 0.7)'
      },
      splitLine: {
        lineStyle: {
          color: 'rgba(255, 255, 255, 0.1)'
        }
      }
    },
    yAxis: {
      type: 'value',
      axisLine: {
        lineStyle: {
          color: 'rgba(255, 255, 255, 0.3)'
        }
      },
      axisLabel: {
        color: 'rgba(255, 255, 255, 0.7)'
      },
      splitLine: {
        lineStyle: {
          color: 'rgba(255, 255, 255, 0.1)'
        }
      }
    },
    series: [
      {
        name: '同比',
        type: 'line',
        data: [120, 150, 135, 165, 180, 210],
        lineStyle: {
          color: 'rgba(255, 198, 0, 1)'
        },
        itemStyle: {
          color: 'rgba(255, 198, 0, 1)'
        },
        areaStyle: {
          color: 'rgba(255, 198, 0, 0.1)'
        },
        smooth: true
      },
      {
        name: '环比',
        type: 'line',
        data: [110, 130, 145, 155, 170, 195],
        lineStyle: {
          color: 'rgba(168, 85, 247, 1)'
        },
        itemStyle: {
          color: 'rgba(168, 85, 247, 1)'
        },
        areaStyle: {
          color: 'rgba(168, 85, 247, 0.1)'
        },
        smooth: true
      }
    ]
  };

  benefitChart.setOption(benefitOption);

  // 监听窗口大小变化，自动调整图表大小
  window.addEventListener('resize', () => {
    energyChart.resize();
    benefitChart.resize();
  });
}
</script>

<style lang="less" scoped>
// 基础样式设置，确保页面占满屏幕且无滚动
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
  overflow: hidden; // 禁止页面滚动
}

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
  min-height: 100vh;
  width: 100vw;
}

// 容器占满整个屏幕
.fdq_container {
  background-image: url("https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/fdq_bg.png");
  background-size: cover;
  position: relative;
  z-index: 200;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  gap: 5px;
}

/* 头部样式 - 固定高度 */
.header-panel {
  background-image: url("https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/fdq_header.png");
  background-repeat: no-repeat;
  background-size: contain;
  background-position: center;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  height: 60px; // 固定高度
  border-radius: 12px;
  flex-shrink: 0; // 不压缩
}

/* 主内容区样式 - 填充计算剩余高度 = 屏幕高度 - 头部高度 */
.fdq_main-panel {
  flex-grow: 1;
  z-index: 100;
  display: flex;
  flex-wrap: wrap;
}

/* 左右统计区域 - 平均分配剩余空间 */
.left-stats,
.right-stats {
  z-index: 99999;
  display: flex;
  flex-direction: column;
  flex: 1;
  gap: 5px;
}

/* 卡片高度按比例分配 */
.left-stats .stat-card,
.right-stats .stat-card {
  flex: 1; // 平均分配高度
  box-sizing: border-box;
  min-height: 300px;
  max-height: calc((100vh - 60px)/3);
  padding: 10px; // 增加内边距
  display: flex;
  flex-direction: column;
}

/* 地图容器 - 占据中间主要空间 */
.map-container {
  flex: 2;
  border-radius: 12px;
  padding: 15px;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
  min-width: 250px; // 最小宽度调整
  //height: 100%; // 占满父容器高度
  box-sizing: border-box;
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
  min-height: 0;
}

/* 地图底部状态条 */
.map-footer {
  z-index: 100;
  display: flex;
  justify-content: center;
  position: relative;
  gap: 15px;
  width: 100%;
  border: 1px;
  font-weight: bold;
  background-color: rgba(0, 0, 0, 0.75);
  bottom: calc(-100% + 210px);
  flex-wrap: wrap;
}

/* 告警信息容器 */
.new-container {
  position: absolute;
  width: calc(100% - 30px); // 减去内边距
  min-height: 250px; // 固定高度
  max-height: 250px; // 固定高度
  min-width: 250px;
  bottom: 0; // 与父容器内边距一致
  //left: 15px;
  border-radius: 12px;
  background: rgba(14, 38, 59, 0.9);
  z-index: 201;
  box-sizing: border-box;
  padding: 10px;
}

.new-container::before{
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
}

.status-item {
  color: white;
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.8rem; // 减小字体
}

.status-dot {
  width: 12px;
  height: 12px;
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

/* 卡片样式调整 */
.stat-card {
  background: rgba(14, 38, 59, 0.9);
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

.stat-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
}

/* 卡片标题样式 */
.card-title {
  border-radius: 20px 0 0 20px;
  background:linear-gradient(90deg, rgba(14, 208, 242, 1)0%,rgba(255, 255, 255, 0) 100%);
  display: flex;
  align-items: center;
  margin-bottom: 5px;
  color: white;
  font-size: 1rem; // 减小字体
  font-weight: 600;
  padding: 5px 10px; // 增加内边距让标题更美观
}

.card-title i {
  margin-right: 10px;
  font-size: 1rem; // 减小图标
  width: 30px; // 减小尺寸
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #66ffff;
}

/* 1. 任务清单样式 */
.task-list-container {
  flex: 1;
  overflow-y: auto;
  padding: 5px 0;
}

.task-item {
  padding: 10px;
  margin-bottom: 5px;
  background: rgba(20, 33, 51, 0.3);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.task-item:hover {
  background: rgba(47, 137, 252, 0.2);
}

.active-task {
  background: linear-gradient(90deg, rgba(47, 137, 252, 0.3)0%,rgba(47, 137, 252, 0) 100%);

  //border-left: 3px solid #2F89FC;
}

.task-vehicle {
  font-weight: bold;
  color: #00F0FF;
  margin-bottom: 3px;
}

.task-name {
  font-size: 0.9rem;
  margin-bottom: 3px;
  color: white;
}

.task-user {
  font-size: 0.8rem;
  color: white;
}

/* 2. 任务详情步骤条 */
.task-steps {
  flex: 1;
  padding: 10px 0;
  overflow-y: auto;
}

::v-deep .vertical-steps {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

::v-deep .el-step__description {
  color: rgba(255, 255, 255, 0.7) !important;
  font-size: 0.8rem !important;
}

::v-deep .el-step__title {
  color: white !important;
  font-size: 0.9rem !important;
}

/* 3. 实时发电数据 */
.realtime-grid {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  padding: 5px 0;
  overflow-y: auto;
}

.realtime-item {
  background: rgba(20, 33, 51, 0.3);
  border-radius: 8px;
  padding: 8px;
  text-align: center;
}

.realtime-label {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 1);
  margin-bottom: 5px;
}

.realtime-value {
  font-size: 1.1rem;
  font-weight: bold;
  color: #00F0FF;
}

/* 4. 维护信息 */
.maintenance-grid {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  padding: 5px 0;
  overflow-y: auto;
}

.maintenance-item {
  background: rgba(20, 33, 51, 0.3);
  border-radius: 8px;
  padding: 10px;
  text-align: center;
}

.maintenance-value {
  font-size: 1.2rem;
  font-weight: bold;
  color: #00F0FF;
  margin-bottom: 5px;
}

.maintenance-label {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 1);
}

/* 5. 发电统计 */
.statistics-grid {
  flex: 1;
  display: flex;
  grid-template-columns: repeat(1, 1fr);
  gap: 10px;
  padding: 5px 0;
  overflow-y: auto;
}

.statistic-item {
  background: rgba(20, 33, 51, 0.3);
  border-radius: 8px;
  padding: 10px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.statistic-icon {
  width: 40px;
  height: 40px;
  min-height: 40px;
  border-radius: 50%;
  background: rgba(102, 255, 255, 0.2);
  color: #66ffff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
}

.statistic-value {
  font-size: 1.3rem;
  font-weight: bold;
  color: #00F0FF;
  margin-bottom: 5px;
}

.statistic-label {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 1);
}

.charts-container{
  display: flex;
  height: 100%;
}

/* 6. 运行效益图表 */
.echart-container {
  width: 100%;
  height: 100%;
}

// 确保图表容器有足够高度
.chart-wrapper {
  flex: 1;
  min-height: 240px;
}

/* 告警信息列表 */
.alarm-list {
  height: calc(100% - 40px);
  overflow: hidden;
  position: relative;
}


.alarm-item {
  padding: 8px 5px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
}

.alarm-time {
  padding: 8px 5px;
  color: #00F0FF;
  margin-right: 10px;
  min-width: 50px;
}

.alarm-content {
  padding: 8px 5px;
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: white;
}

/* 响应式调整 */
@media (max-width: 1400px) {
  .left-stats,
  .right-stats {
    min-width: 250px;
  }

  .map-container {
    min-width: 250px;
  }
}

@media (max-width: 992px) {
  .fdq_main-panel {
    flex-direction: column;
    overflow-y: auto; // 小屏幕允许纵向滚动
  }

  .map-container {
    min-width: 100%;
    flex: none;
    height: 800px;
  }

  .left-stats, .right-stats {
    min-width: 100%;
  }
}

@media (max-width: 768px) {
  .map-footer {
    flex-direction: column;
    align-items: center;
    bottom: 100px;
  }

  .new-container {
    height: 250px;
  }

  .card-title {
    font-size: 0.9rem;
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

// 滚动条样式美化
::-webkit-scrollbar {
  width: 1px;
  height: 1px;
}

::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255,0);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0);
}
</style>
