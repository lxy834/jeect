<template>
  <div class="dashboard-container">
    <div class="header-panel">
      <div style="width: 100%">
       <div class="icon-white" style="text-align: center;width: 100%;color:'#ffffff';">
          <span style="font-weight:800;font-size:1rem;letter-spacing:1px">{{device}}</span>
          <i style="font-size:2rem;float:right;margin-right:15px;cursor: pointer; align-items: center;" class="fas fa-expand"  v-show="state.show" @click="exit(true)"/>
          <i style="font-size:2rem;float:right;margin-right:15px;cursor: pointer; align-items: center;" class="fa fa-compress" v-show="!state.show" @click="exit(false)"/>

       </div>
</div>
    </div>
    <!-- 主内容区 -->
    <div class="main-panel" id="map">
      <div class="left-stats">
        <div class="stat-card">
          <div class="device-stats">
            <dv-border-box-9 :color="['#66FFFF', '#66FFFF']" style="height:100%;width:100%;min-height:200px;">
              <div class="card-title">
                <i class="	fa fa-bolt" />
                电压
              </div>
              <div id="voltage" style="height:88%;width:98%;margin-left:2%;min-height:180px;"></div>
            </dv-border-box-9>
          </div>
        </div>
        <div class="stat-card">
          <div class="event-stats">
            <dv-border-box-9 :color="['#66FFFF', '#66FFFF']" style="height:100%;width:100%;min-height:200px;">
              <div class="card-title">
                <i class="fas fa-microchip"></i>
                有功
              </div>
              <div id="active" style="height:88%;width:98%;margin-left:2%;min-height:180px;"></div>
            </dv-border-box-9>
          </div>
        </div>
      </div>
      <div class="center-stats">
        <!-- 信道信息 -->
        <div class="stat-card">
          <div class="event-stats">
            <dv-border-box-9 :color="['#66FFFF', '#66FFFF']" style="height:100%;width:100%;min-height:200px;">
              <div class="card-title">
                <i class="fas fa-bar-chart" />
                线路图
              </div>
              <div style="height:86%;width:95%;background-image:url('https://pic1.zhimg.com/0aacef55810d4949d28f3f7a1147aad0_r.jpg');
              background-size:100% 100%;min-height:180px;margin-left:3%;margin-top: 4px;background-repeat: no-repeat"></div>
            </dv-border-box-9>
          </div>
        </div>
        <div class="stat-card">
          <div class="event-stats">
            <dv-border-box-9 :color="['#66FFFF', '#66FFFF']" style="height:100%;width:100%;min-height:200px;">
              <div class="card-title">
                <i class="fa fa-pie-chart" />
                事件统计
              </div>
              <div id="event" style="height:88%;width:98%;margin-left:2%;min-height:180px;"></div>
            </dv-border-box-9>
          </div>
        </div>
      </div>
      <div class="right-stats">
        <!-- 控制功能 -->
        <div class="stat-card">
          <div class="event-stats">
            <dv-border-box-9 :color="['#66FFFF', '#66FFFF']" style="height:100%;width:100%;min-height:200px;">
              <div class="card-title">
                <i class="	fa fa-font"></i>
                电流
              </div>
              <div id="current" style="height:88%;width:98%;margin-left:2%;min-height:180px;"></div>
            </dv-border-box-9>
          </div>
        </div>
        <div class="stat-card">
          <div class="event-stats">
            <dv-border-box-9 :color="['#66FFFF', '#66FFFF']" style="height:100%;width:100%;min-height:200px;">
              <div class="card-title">
                <i class="fa fa-area-chart" />
                功率因素
              </div>
              <div id="factor" style="height:88%;width:98%;margin-left:2%;min-height:180px;"></div>
            </dv-border-box-9>
          </div>
        </div>
      </div>

      <el-dialog  v-model="state.dialogVisible" :title="state.legendName" draggable>
        <el-table :header-cell-style="{background:'rgba(14, 38, 59, 0.95)',color:'#66ffff'}"
                  :cell-style="{background:'rgba(14, 38, 59, 0.95)',color:'#ffffff'}" :data="state.tableData" height="200" stripe style="width: 100%">
          <el-table-column prop="deviceType" label="事件类型"/>
          <el-table-column prop="warnInfo" label="事件详情" show-overflow-tooltip/>
          <el-table-column prop="lineLocation" label="线路名称" show-overflow-tooltip/>
          <el-table-column prop="warnTime" label="触发时间" show-overflow-tooltip/>
        </el-table>
        <el-pagination style="margin-top: 5px" size="small" layout="->,prev, pager, next" :total="state.total" :background="true"/>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import screenfull from "screenfull";
import { useAppStore } from "@/store/modules/app";
import {  onMounted, reactive, watchEffect } from "vue";
import { useMenuSetting } from "/src/hooks/setting/useMenuSetting";
import "@fortawesome/fontawesome-free/css/all.css";
import { onBeforeRouteLeave } from "vue-router";
import { useRoute } from 'vue-router';
import { computed } from 'vue';
import * as echarts from 'echarts';
const appStore = useAppStore();
import {  getVolumeById } from "@/views/ftu/list/FtuDevice.api.ts";
import {  getStatById,getEventById } from "@/views/ftu/warn/FtuWarnInfo.api.ts";
import { defHttp } from "@/utils/http/axios";
const {  setMenuSetting } = useMenuSetting();
const route = useRoute();
const id = computed(() => route.query.ftu );
const device = computed(() => route.query.device );
const state = reactive({
  show: true,
  dialogVisible: false,
  timeList:[],
  activeList:[],
  currentList:[],
  factorList:[],
  voltageList:[],
  warnList: [],
  legendName:"",
  eventTime:[],
  eventData:{},
  page:1,
  tableData:[],
  total:0,
  pageSize:10
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

function initEcharts(){

  var voltage = document.getElementById('voltage');
  var voltageChart = echarts.init(voltage);
  var voltageOption;
  voltageOption = {
    textStyle:{
      color:'#FFFFFF'
    },
    title: {
      text: ''
    },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(0,0,0,.5)', // 添加背景颜色
      borderColor: '#333', // 添加边框颜色
      borderWidth: 1, // 添加边框宽度
      textStyle: {
        color: '#fff' // 添加文本颜色
      },
      // 添加formatter函数来自定义tooltip显示格式
      formatter: function(params) {
        let result = params[0].name + '<br/>';
        params.forEach(item => {
          // 为数值添加kV单位
          result += '<span style="color:' + item.color + '">●</span> ' +
            item.seriesName + ': ' + item.value + ' kV<br/>';
        });
        return result;
      }
    },
    toolbox: {
      show: true,
      iconStyle: {
        normal: {
          color: '#FFFFFF' // 设置所有图标为白色
        },
        emphasis: {
          color: '#FFFFFF' // 可选：设置悬停时的颜色
        }
      },
      feature: {
        dataZoom: {
          yAxisIndex: 'none'
        },
        magicType: { type: ['line', 'bar'] },
        restore: {},
      },

    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: state.timeList
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value} kV'
      }
    },
    series: [
      {
        name: '电压值',
        type: 'line',
        data: state.voltageList,

        lineStyle: {
          color: '#36CBCB' // 设置线条颜色为青色
        },
        itemStyle: {
          color: '#36CBCB' // 设置标记点的颜色
        },
        markPoint: {
          label: {
            textStyle: {
              color: '#FFFFFF' // 设置标记线文字颜色为白色
            }
          },
          data: [
            { type: 'max', name: 'Max' },
            { type: 'min', name: 'Min' }
          ]
        },
        markLine: {
          label: {
            textStyle: {
              color: '#FFFFFF' // 设置标记线文字颜色为白色
            }
          },
          data: [{ type: 'average', name: 'Avg' }]
        }
      }
    ]
  };
  voltageOption && voltageChart.setOption(voltageOption);

  var active = document.getElementById('active');
  var activeChart = echarts.init(active);
  var activeOption;
  activeOption = {
    textStyle:{
      color:'#FFFFFF'
    },
    title: {
      text: ''
    },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(0,0,0,.5)', // 添加背景颜色
      borderColor: '#333', // 添加边框颜色
      borderWidth: 1, // 添加边框宽度
      textStyle: {
        color: '#fff' // 添加文本颜色
      },
      // 添加formatter函数来自定义tooltip显示格式
      formatter: function(params) {
        let result = params[0].name + '<br/>';
        params.forEach(item => {
          // 为数值添加kV单位
          result += '<span style="color:' + item.color + '">●</span> ' +
            item.seriesName + ': ' + item.value + ' MW<br/>';
        });
        return result;
      }
    },
    toolbox: {
      show: true,
      iconStyle: {
        normal: {
          color: '#FFFFFF' // 设置所有图标为白色
        },
        emphasis: {
          color: '#FFFFFF' // 可选：设置悬停时的颜色
        }
      },
      feature: {
        dataZoom: {
          yAxisIndex: 'none'
        },
        magicType: { type: ['line', 'bar'] },
        restore: {},
      },

    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: state.timeList
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value} MW'
      }
    },
    series: [
      {
        name: '有功功率',
        type: 'line',
        data: state.activeList,

        lineStyle: {
          color: '#36CBCB' // 设置线条颜色为青色
        },
        itemStyle: {
          color: '#36CBCB' // 设置标记点的颜色
        },
        markPoint: {
          label: {
            textStyle: {
              color: '#FFFFFF' // 设置标记线文字颜色为白色
            }
          },
          data: [
            { type: 'max', name: 'Max' },
            { type: 'min', name: 'Min' }
          ]
        },
        markLine: {
          label: {
            textStyle: {
              color: '#FFFFFF' // 设置标记线文字颜色为白色
            }
          },
          data: [{ type: 'average', name: 'Avg' }]
        }
      }
    ]
  };
  activeOption && activeChart.setOption(activeOption);

  var current = document.getElementById('current');
  var currentChart = echarts.init(current);
  var currentOption;
  currentOption = {
    textStyle:{
      color:'#FFFFFF'
    },
    title: {
      text: ''
    },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(0,0,0,.5)', // 添加背景颜色
      borderColor: '#333', // 添加边框颜色
      borderWidth: 1, // 添加边框宽度
      textStyle: {
        color: '#fff' // 添加文本颜色
      },
      // 添加formatter函数来自定义tooltip显示格式
      formatter: function(params) {
        let result = params[0].name + '<br/>';
        params.forEach(item => {
          // 为数值添加kV单位
          result += '<span style="color:' + item.color + '">●</span> ' +
            item.seriesName + ': ' + item.value + ' A<br/>';
        });
        return result;
      }
    },

    toolbox: {
      show: true,
      iconStyle: {
        normal: {
          color: '#FFFFFF' // 设置所有图标为白色
        },
        emphasis: {
          color: '#FFFFFF' // 可选：设置悬停时的颜色
        }
      },
      feature: {
        dataZoom: {
          yAxisIndex: 'none'
        },
        magicType: { type: ['line', 'bar'] },
        restore: {},
      },

    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: state.timeList
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value} A'
      }
    },
    series: [
      {
        name: '电流值',
        type: 'line',
        data: state.currentList,

        lineStyle: {
          color: '#36CBCB' // 设置线条颜色为青色
        },
        itemStyle: {
          color: '#36CBCB' // 设置标记点的颜色
        },
        markPoint: {
          label: {
            textStyle: {
              color: '#FFFFFF' // 设置标记线文字颜色为白色
            }
          },
          data: [
            { type: 'max', name: 'Max' },
            { type: 'min', name: 'Min' }
          ]
        },
        markLine: {
          label: {
            textStyle: {
              color: '#FFFFFF' // 设置标记线文字颜色为白色
            }
          },
          data: [{ type: 'average', name: 'Avg' }]
        }
      }
    ]
  };
  currentOption && currentChart.setOption(currentOption);

  var factor = document.getElementById('factor');
  var factorChart = echarts.init(factor);
  var factorOption;
  factorOption = {
    textStyle:{
      color:'#FFFFFF'
    },
    title: {
      text: ''
    },
    tooltip: {
      trigger: 'axis'
    },

    toolbox: {
      show: true,
      iconStyle: {
        normal: {
          color: '#FFFFFF' // 设置所有图标为白色
        },
        emphasis: {
          color: '#FFFFFF' // 可选：设置悬停时的颜色
        }
      },
      feature: {
        dataZoom: {
          yAxisIndex: 'none'
        },
        magicType: { type: ['line', 'bar'] },
        restore: {},
      },

    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: state.timeList
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value} cosφ'
      }
    },
    series: [
      {
        name: '功率因素',
        type: 'line',
        data: state.factorList,

        lineStyle: {
          color: '#36CBCB' // 设置线条颜色为青色
        },
        itemStyle: {
          color: '#36CBCB' // 设置标记点的颜色
        },
        markPoint: {
          label: {
            textStyle: {
              color: '#FFFFFF' // 设置标记线文字颜色为白色
            }
          },
          data: [
            { type: 'max', name: 'Max' },
            { type: 'min', name: 'Min' }
          ]
        },
        markLine: {
          label: {
            textStyle: {
              color: '#FFFFFF' // 设置标记线文字颜色为白色
            }
          },
          data: [{ type: 'average', name: 'Avg' }]
        }
      }
    ]
  };
  factorOption && factorChart.setOption(factorOption);

  var event = document.getElementById('event');
  var eventChart = echarts.init(event);
  var eventOption;
  eventOption = {
    textStyle:{
      color:'#FFFFFF'
    },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(0,0,0,.5)', // 添加背景颜色
      borderColor: '#333', // 添加边框颜色
      borderWidth: 1, // 添加边框宽度
      textStyle: {
        color: '#fff' // 添加文本颜色
      },
      // 添加formatter函数来自定义tooltip显示格式
      formatter: function(params) {
        let result = params[0].name + '<br/>';
        params.forEach(item => {
          // 为数值添加kV单位
          result += '<span style="color:' + item.color + '">●</span> ' +
            item.seriesName + ': ' + item.value + ' 次<br/>';
        });
        return result;
      }
    },
    legend: {
      textStyle:{
        color:'#FFFFFF'
      },
      data: ['主站指令', '故障报警','通信异常']
    },
    toolbox: {
      show: true,
      iconStyle: {
        normal: {
          color: '#FFFFFF' // 设置所有图标为白色
        },
        emphasis: {
          color: '#FFFFFF' // 可选：设置悬停时的颜色
        }
      },
      feature: {
        dataZoom: {
          yAxisIndex: 'none'
        },
        magicType: { type: ['line', 'bar'] },
        restore: {},
      }
    },
    calculable: true,
    xAxis: [
      {
        type: 'category',
        data: state.eventTime
      }
    ],
    yAxis: [
      {
        type: 'value'
      }
    ],
    series: [
      {
        name: '主站指令',
        type: 'bar',
        data: state.eventData.switch,
        itemStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1, // 从下到上的渐变
            colorStops: [
              { offset: 0, color: 'rgb(2,152,255)' }, // 顶部颜色（透明度80%）
              { offset: 1, color: 'rgba(0, 191, 213, 0.2)' }  // 底部颜色（透明度20%）
            ]
          }
        },
        markPoint: {

          data: [
            { type: 'max', name: 'Max' },
            { type: 'min', name: 'Min' }
          ]
        },
      },
      {
        name: '故障报警',
        type: 'bar',
        data:  state.eventData.warn,
        itemStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1, // 从下到上的渐变
            colorStops: [
              { offset: 0, color: 'rgb(138,99,255)' }, // 顶部颜色（透明度80%）
              { offset: 1, color: 'rgba(0, 191, 213, 0.2)' }
            ]
          }
        },
        markPoint: {
          data: [
            { type: 'max', name: 'Max' },
            { type: 'min', name: 'Min' }
          ]
        },
      },
      {
        name: '通信异常',
        type: 'bar',
        data:  state.eventData.comm,
        itemStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1, // 从下到上的渐变
            colorStops: [
              { offset: 0, color: 'rgba(68, 215, 182, 1)' }, // 顶部颜色（透明度80%）
              { offset: 1, color: 'rgba(0, 191, 213, 0.2)' }
            ]
          }
        },
        markPoint: {
          data: [
            { type: 'max', name: 'Max' },
            { type: 'min', name: 'Min' }
          ]
        },

      }
    ]
  };
  eventOption && eventChart.setOption(eventOption);
  eventChart.on('click', async function(param) {
    const legendName = param.seriesName;
    const yValue = param.value;
    if(yValue===0){
      return
    }
    state.legendName = device.value + "---" + legendName + param.name
    let params = {
      ftuId: id.value,
      Date: '2025-' + param.name,
      event:legendName,
      pageNo: state.page,
      pageSize: state.pageSize
    }
    await defHttp.get({url:getEventById,params}).then(data =>{
      state.tableData = data.records
      state.total = data.total
      state.dialogVisible = true
    })

  });

  window.addEventListener("resize",function (){
    voltageChart.resize();
    activeChart.resize();
    currentChart.resize();
    factorChart.resize();
    eventChart.resize();
  });
}

async function getData() {
  let params = {
    ftuId: id.value
  }

  await defHttp.get({url:getStatById,params}).then(data =>{
    state.eventTime = data.xAxisData
    state.eventData = data.seriesData
  })

  await defHttp.get({url:getVolumeById, params}).then(data => {
    state.timeList = data.createTimeList;
    state.factorList = data.factorList;
    state.activeList = data.activePowerList;
    state.currentList = data.ftuCurrentList;
    state.voltageList = data.voltageList

  })
  initEcharts();
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

  setMenuSetting({ show: true });
  appStore.setLayoutHideHeader(true);
  appStore.setLayoutHideMultiTabs(true);
  appStore.setLayoutHideSider(true);
  state.show = true;
  getData()
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
  min-width: 800px;
  margin: 0 10px 0 10px;
}

.left-stats,
.right-stats {
  z-index: 100;
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 400px;
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

::v-deep .el-table__body-wrapper{
  background: rgba(14, 38, 59, 0.9);
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
