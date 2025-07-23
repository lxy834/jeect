<template>
  <div style="height: 100%;width: 100%" id="track-map" class="trackMap">

  </div>
</template>

<script setup lang="ts">
import { track } from "@/views/fdq/property/FdqProperty.api";
import { computed, onMounted, reactive, ref } from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
import { useRoute } from "vue-router";
import { defHttp } from "@/utils/http/axios";
import { ElMessage } from "element-plus";
import { GraspRoadPath } from "@/utils/graspRoadPath";
import { truck } from "@/assets/images";
const route = useRoute();
const plateNumber = computed(() => route.query.plateNumber );
window._AMapSecurityConfig = {
  securityJsCode: "d1243371803f635fdfa7b253ffb723e0" // 安全密钥
};
const state = reactive({
  map:null,
  strTime:'',
  endTime:'',
  basicInfo:[],
  basicInfoReversal:[],
  lineArr:[],
  marker:null
})
const correction = ref(false);
async function processArray(lineArr: any[]): Promise<any[]> {
  const chunkSize = 500;
  const chunks: any[][] = [];

  // 将数组分割成多个子数组，每个子数组最多500个元素
  for (let i = 0; i <= lineArr.length; i += chunkSize) {
    chunks.push(lineArr.slice(i, Math.min(i + chunkSize, lineArr.length)));
  }

  var grasp = new AMap.GraspRoad();

  // 异步处理每个子数组，并保持顺序
  const results = await Promise.all(chunks.map(async (chunk, index) => {
    let processedChunk = [] as any[]; // 假设 grasp.driving 返回的是一个处理后的数组
    let firstTime = chunk[0].tm;
    chunk = chunk.map(item => ({
      ...item,
      tm: item.tm === firstTime ? item.tm : item.tm - firstTime
    }));
    return new Promise<{ index: number, processedChunk: any[] }>((resolve, reject) => {
      grasp.driving(chunk, (error: any, result: any) => {
        if (!error) {
          processedChunk = result.data.points; //纠偏后的轨迹'
          resolve({ index, processedChunk }); // 成功时，调用 resolve
        } else {
          // reject(error); // 如果有错误，调用 reject
          console.log("chunk", chunk);
          resolve({ index, processedChunk: chunk }); // 成功时，调用 resolve
        }
      });
    });

  }));

  // 按原始顺序合并处理结果
  results.sort((a, b) => a.index - b.index);

  // 合并所有处理后的子数组
  const finalResult = results.flatMap(result => result.processedChunk);
  return finalResult;
}


async function getTrack() {
  let params = {
    plateNumber:plateNumber.value
  }
  const [trackRes] = await Promise.all([
    defHttp.post({url:track,params})
  ])
  if (trackRes.length === 0) {
    ElMessage.info("没有轨迹记录");
    state.basicInfo = [];
    return;
  }
  state.basicInfo = trackRes;
  state.basicInfoReversal = [...trackRes].reverse();
  state.lineArr = state.basicInfo.map((item) => ({
    x: item.lng,
    y: item.lat,
    sp: item.speed,
    ag: 0,
    tm: item.timestamp
  }));
  state.map.clearMap();
  var line: GraspRoadPath[] = state.lineArr;
  if (correction.value){
    line = await processArray(line);
  }

  let lineArr: number[][] = line.map(item => [item.x, item.y]);

  const map = state.map

  for (let i = 0; i < lineArr.length; i++) {
    const demo = new AMap.Marker({
      map,
      position: lineArr[i],
    });
    // demo.setTitle(state.basicInfo[i].createTime)
//     demo.setLabel({
//       direction:'top',
//       offset: new AMap.Pixel(10, 0),  //设置文本标注偏移量
//       content: `<div style="color: white">时间：${state.basicInfo[i].createTime}</div>
// <div style="color: white">坐标：${state.basicInfo[i].lng},${state.basicInfo[i].lat}</div>
// <div style="color: white">速度：${state.basicInfo[i].speed}</div>`, //设置文本标注内容
//     });
  }

  new AMap.Polyline({
    map,
    path: lineArr,
    showDir: true,
    strokeColor: "#00ffff",  //线颜色
    strokeWeight: 6      //线宽
  });

  var passedPolyline = new AMap.Polyline({
    map,
    strokeColor: "#AF5",  //线颜色
    strokeWeight: 6      //线宽
  });
  var icon = new AMap.Icon({
    image: truck,
    imageSize: new AMap.Size(40, 40)   // 根据所设置的大小拉伸或压缩图片
  });

  var marker = new AMap.Marker({
    map,
    position: lineArr[lineArr.length - 1],
    offset: new AMap.Pixel(-20, -20),
    icon
  });
  state.marker = marker;
  marker.on("moving", (e) => {
    passedPolyline.setPath(e.passedPath);
    state.map.setCenter(e.target.getPosition(), true);
  });

  state.map.setFitView();

  window.startAnimation = function startAnimation() {
    marker.moveAlong(lineArr, {
      // 每一段的时长
      duration: state.duration,//可根据实际采集时间间隔设置
      // JSAPI2.0 是否延道路自动设置角度在 moveAlong 里设置
      autoRotation: true
    });
  };

  window.pauseAnimation = function() {
    marker.pauseMove();
  };

  window.resumeAnimation = function() {
    marker.resumeMove();
  };

  window.stopAnimation = function() {
    marker.stopMove();
  };


}

function initMap() {
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
      "AMap.MarkerClusterer",
      "AMap.GraspRoad"
    ]
  }).then((AMap) => {
    state.map = new AMap.Map("track-map", {
      center: [104.830389, 26.592528],
      zoom: 11,
      mapStyle: "amap://styles/macaron"
    });
    getTrack()
  }).catch((e) => {
    console.error("地图加载失败:", e);
  });
}

onMounted(()=>{
  initMap()
})

</script>

<style scoped lang="less">
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

.trackMap{
  background-image: url("https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/fdq_bg.png");
  background-size: cover;
  position: relative;
  z-index: 200;
  min-height: 100vh;
}
</style>
