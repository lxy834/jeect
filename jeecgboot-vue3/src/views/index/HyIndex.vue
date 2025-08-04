<template>
  <div class="fdq_container" :style="containerStyle">
    <!-- 悬浮按钮容器 -->
    <div
      class="floating-button-container"
      ref="floatingContainer"
      @mousedown="startDrag"
      style="position: fixed; z-index: 9999; cursor: move;"
    >
      <!-- 主悬浮按钮 -->
      <button
        class="floating-button"
        @click="toggleDrawer"
        @mouseenter="showTooltip('mainTooltip')"
        @mouseleave="hideTooltip('mainTooltip')"
        style="cursor: pointer;"
      >
        <i class="fas fa-palette"></i>
      </button>
      <div
        class="tooltip"
        :class="{ 'tooltip-visible': state.tooltips.mainTooltip }"
        ref="mainTooltip"
      >
        个性化配置
      </div>
    </div>

    <!-- 个性化配置抽屉 -->
    <el-drawer
      title="个性化配置"
      :model-value="drawerVisible"
      :direction="'rtl'"
      :size="550"
      @close="drawerVisible = false"
    >
      <div class="config-container">
        <!-- 字体颜色配置 -->
        <div class="config-item">
          <label class="config-label" style="color: black;font-weight: bold">文字颜色</label>
          <el-color-picker
            v-model="customConfig.fontColor"
            :predefine="predefinedColors"
            class="color-picker"
            :show-alpha="true"
          ></el-color-picker>
          <label class="config-label" style="color: black;font-weight: bold">数值颜色</label>
          <el-color-picker
            v-model="customConfig.numberColor"
            :predefine="predefinedColors"
            class="color-picker"
          ></el-color-picker>
          <label class="config-label" style="color: black;font-weight: bold">标题颜色</label>
          <el-color-picker
            v-model="customConfig.titleColor"
            :predefine="predefinedColors"
            class="color-picker"
            :show-alpha="true"
          ></el-color-picker>
        </div>

        <!-- 背景图片配置 -->
        <div class="config-item">
          <label class="config-label" style="color: black;font-weight: 800">背景图片</label>
          <div style="display: flex;gap: 5px">
            <div v-for="item in state.classList">
              <img
                :src="item.url"
                @click="customConfig.bgImage=item.url"
                style="width: 100px; height: 100px; cursor: pointer"
              />
            </div>
            <div style="width: 100px; height: 100px; cursor: pointer;border: 1px solid black"  @click="customConfig.bgImage=''"></div>
          </div>
        </div>
        <label class="config-label" style="color: black;font-weight: 800">头部样式</label>
        <el-select
          v-model="customConfig.headerImg"
          placeholder="选择头部样式"
          class="select-control"
        >
          <!--          <el-option label="默认" value="https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/head.png"></el-option>-->
          <el-option label="黑色" value="https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/head.png"></el-option>
          <el-option label="白色" value="https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/header_b.png"></el-option>
        </el-select>
        <!-- 地图样式配置 -->
<!--        <div class="config-item">-->
<!--          <label class="config-label" style="color: black;font-weight: 800">地图样式</label>-->
<!--          <el-select-->
<!--            v-model="customConfig.mapStyle"-->
<!--            placeholder="选择地图样式"-->
<!--            class="select-control"-->
<!--          >-->
<!--            <el-option label="默认" value="amap://styles/macaron"></el-option>-->
<!--            <el-tooltip-->
<!--              class="box-item"-->
<!--              effect="dark"-->
<!--              content="透明地图搭配背景图效果更佳"-->
<!--              placement="right-start"-->
<!--            >-->
<!--              <el-option label="透明" value="amap://styles/d86da4c2ed42be8272eb068059df8719"></el-option>-->
<!--            </el-tooltip>-->
<!--            <el-option label="标准" value="amap://styles/normal"></el-option>-->
<!--            <el-option label="涂鸦" value="amap://styles/graffiti"></el-option>-->
<!--            <el-option label="远山黛" value="amap://styles/whitesmoke"></el-option>-->
<!--            <el-option label="幻影黑" value="amap://styles/dark"></el-option>-->
<!--            <el-option label="草色青" value="amap://styles/fresh"></el-option>-->
<!--            <el-option label="极夜蓝" value="amap://styles/darkblue"></el-option>-->
<!--            <el-option label="靛青蓝" value="amap://styles/blue"></el-option>-->
<!--            <el-option label="月光银" value="amap://styles/light"></el-option>-->
<!--            <el-option label="雅士灰" value="amap://styles/grey"></el-option>-->
<!--          </el-select>-->
<!--          <img src="https://a.amap.com/lbs/static/img/doc/doc_1699000521194_b00d7.jpeg" style="width:100%;height:auto" data-spm-anchor-id="0.0.0.i4.25166ec7bkGKuO">-->
<!--        </div>-->

<!--        <el-select-->
<!--          v-model="state.showSate"-->
<!--          placeholder="选择地图图层"-->
<!--          class="select-control"-->
<!--        >-->
<!--          &lt;!&ndash;          <el-option label="默认" value="https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/head.png"></el-option>&ndash;&gt;-->
<!--          <el-option label="标准图层" value="标准图层"></el-option>-->
<!--          <el-option label="卫星图层" value="卫星图层"></el-option>-->
<!--        </el-select>-->

        <!-- 卡片背景透明度 -->
        <div class="config-item">
          <label class="config-label" style="color: black;font-weight: 800">卡片透明度</label>
          <el-slider
            v-model="customConfig.cardOpacity"
            :min="0.1"
            :max="1"
            :step="0.1"
            class="slider-control"
          ></el-slider>
          <span class="opacity-value">{{ customConfig.cardOpacity.toFixed(1) }}</span>
        </div>

        <div class="config-actions">
          <el-button @click="resetConfig">重置默认</el-button>
          <el-button type="primary" @click="saveConfig">保存配置</el-button>
        </div>
      </div>
    </el-drawer>

    <div class="header-panel" :style="headerStyle">
      <div style="text-align: right;width: 100%">
        <div class="qp icon-white">
          <i  id="screenFull" class="fas fa-expand"  v-show="state.show" @click="exit(true)"/>
          <i class="fa fa-compress" v-show="!state.show" @click="exit(false)"/>
        </div>
      </div>
    </div>
    <div class="fdq_main-panel" :style="mainPanelStyle">
      <div class="size-full " ref="container" style="width: 100%; max-height: calc(100vh - 60px);position: absolute;z-index: 99999"/>
      <div class="left-stats">
        <!-- 1. 任务清单 -->
        <div class="stat-card" :style="cardStyle">
          <div class="card-title" :style="titleStyle">
            <i class="fas fa-clipboard-list"></i>
            设备清单
          </div>

        </div>

        <!-- 2. 任务详情 -->
        <div class="stat-card" :style="cardStyle">
          <div class="card-title" :style="titleStyle">
            <i class="fas fa-microchip"></i>
<!--            鲁棒性-->
            稳健特性
          </div>

        </div>

        <!-- 3. 实时发电 -->
        <div class="stat-card" :style="cardStyle">
          <div class="card-title" :style="titleStyle">
            <i class="fa fa-bolt" />
            发电计量
          </div>
          <div class="realtime-grid"></div>
        </div>
      </div>
      <div class="map-container" :style="mapContainerStyle">
      </div>
      <div class="right-stats">
        <!-- 4. 维护信息 -->
        <div class="stat-card" :style="cardStyle">
          <div class="card-title" :style="titleStyle">
            <i class="fas fa-clipboard-list"></i>
            水文气象
          </div>
          <div class="maintenance-grid"></div>
        </div>

        <!-- 5. 发电统计 -->
        <div class="stat-card" :style="cardStyle">
          <div class="card-title" :style="titleStyle">
            <i class="fas fa-sliders-h"></i>
            形变趋势
          </div>
          <div class="statistics-grid"></div>
          <div class="statistics-grid" style="display: flex"></div>
        </div>

        <!-- 6. 运行效益 -->
        <div class="stat-card" :style="cardStyle">
          <div class="card-title" :style="titleStyle">
            <i class="fas fa-sliders-h"></i>
            运行告警
          </div>
          <div class="charts-container"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import * as THREE from 'three'
import { onMounted, reactive, ref, onUnmounted, computed, unref } from "vue";
import { useAppStore } from "@/store/modules/app";
import { useMenuSetting } from "@/hooks/setting/useMenuSetting";
import { ElDrawer, ElColorPicker, ElSelect, ElOption, ElSlider, ElButton } from 'element-plus';
import { useGo } from "@/hooks/web/usePage";
import screenfull from "screenfull";
import { onBeforeRouteLeave } from "vue-router";
const appStore = useAppStore();
const { setMenuSetting } = useMenuSetting();
const { toggleCollapsed } = useMenuSetting();
const { getCollapsed } = useMenuSetting();
import { OrbitControls } from "three/examples/jsm/controls/OrbitControls";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
// 获取当前缩放状态（注意：getCollapsed 是计算属性，需要用 unref 或 .value 访问其值）
const isCollapsed = unref(getCollapsed); // true 表示收缩，false 表示展开
window._AMapSecurityConfig = {
  securityJsCode: "d1243371803f635fdfa7b253ffb723e0" // 安全密钥
};

// 新增：个性化配置相关
const drawerVisible = ref(false);

// 预设颜色
const predefinedColors = [
  '#FFFFFF', '#000000', '#FF0000', '#00FF00', '#0000FF',
  '#FFFF00', '#00FFFF', '#FF00FF', '#00F0FF'
];

// 默认配置
const defaultConfig = {
  fontColor: '#FFFFFF',
  numberColor:'#00F0FF',
  titleColor:'linear-gradient(90deg, rgba(14, 208, 242, 1)0%,rgba(255, 255, 255, 0) 100%)',
  bgImage: '',
  mapStyle: 'amap://styles/macaron',
  cardOpacity: 0.35,
  headerImg:'https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/head.png',
};

// 从缓存加载配置或使用默认配置
const loadConfig = () => {
  const saved = localStorage.getItem('hyCustomConfig');
  return saved ? JSON.parse(saved) : { ...defaultConfig };
};

// 个性化配置响应式变量
const customConfig = reactive(loadConfig());

// 切换抽屉显示状态
const toggleDrawer = () => {
  drawerVisible.value = !drawerVisible.value;
};

// 保存配置到本地缓存
const saveConfig = () => {
  localStorage.setItem('hyCustomConfig', JSON.stringify(customConfig));
  // 关闭抽屉
  drawerVisible.value = false;
};

// 重置为默认配置
const resetConfig = () => {
  Object.assign(customConfig, defaultConfig);
};

// 计算属性：动态样式
const containerStyle = computed(() => ({
  color: customConfig.fontColor,
  backgroundImage: customConfig.bgImage ? `url("${customConfig.bgImage}")` : 'none',
  backgroundSize: 'cover',
  backgroundAttachment: 'fixed'
}));

const headerStyle = computed(() => ({
  color: customConfig.fontColor,
  backgroundImage: `url("${customConfig.headerImg}")`,
  backgroundSize: 'contain',
  backgroundRepeat: 'no-repeat',
  backgroundPosition: 'center',
  width: '100%',
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  position: 'relative',
  height: '60px', // 固定高度
  borderRadius:'12px',
  flexShrink: 0 // 不压缩
}));

const mainPanelStyle = computed(() => ({
  color: customConfig.fontColor
}));

const number = computed(()=>({
  color:customConfig.numberColor
}))

const cardStyle = computed(() => ({
  background: `rgba(14, 38, 59, ${customConfig.cardOpacity})`
}));

const mapContainerStyle = computed(() => ({
  color: customConfig.fontColor
}));

const titleStyle = computed(()=>({
  background:customConfig.titleColor
}))

const state = reactive({
  classList:[
    {
      id:1,
      url:'https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/118.png'
    },
    {
      id:2,
      url:'https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/bg_ftu.png'
    },
    {
      id:3,
      url:'https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/qianse.png'
    },
  ],
  show:true,
  isOpen:false,
  tooltips: {
    mainTooltip: false,
    listTooltip: false,
    fontTooltip: false,
    helpTooltip: false
  }
})

const floatingContainer = ref(null);
const dragState = reactive({
  isDragging: false,
  startX: 0,
  startY: 0,
  initialX: 0,
  initialY: 0,
  hasMoved: false  // 用于判断是否发生了拖动
});

function showTooltip(tooltipName) {
  state.tooltips[tooltipName] = true;
}

function hideTooltip(tooltipName) {
  state.tooltips[tooltipName] = false;
}
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

const setCanvasHeight = (isFullScreen: boolean) => {
  // 获取容器内的canvas元素
  const containerEl = container.value;
  if (!containerEl) return;

  const canvas = containerEl.querySelector('canvas');
  if (canvas) {
    if (isFullScreen) {
      // 全屏时设置高度100%
      canvas.style.height = 'calc(100% - 600px)';
      // 同时让容器高度也充满全屏
      // containerEl.style.maxHeight = '100%';
    } else {
      // 退出全屏时恢复默认高度（根据容器高度自适应）
      canvas.style.height = '100%'; // 保持自适应容器
      containerEl.style.maxHeight = 'calc(100vh - 60px)';
    }
    // 触发渲染更新
    renderer?.setSize(containerEl.clientWidth, containerEl.clientHeight);
  }
};

function exit(v: boolean) {
  if (v) {
    state.show = false;
    screenfull.request().then(() => {
      // 进入全屏后设置canvas高度
      setCanvasHeight(true);
    });
    setMenuSetting({ show: false });
    appStore.setLayoutHideHeader(true);
    appStore.setLayoutHideSider(true);
    appStore.setLayoutHideMultiTabs(true);
  } else {
    state.show = true;
    screenfull.exit().then(() => {
      // 退出全屏后恢复canvas高度
      setCanvasHeight(false);
    });
    setMenuSetting({ show: true });
    appStore.setLayoutHideHeader(true);
    appStore.setLayoutHideMultiTabs(true);
    appStore.setLayoutHideSider(true);
  }
}

onBeforeRouteLeave(() => {
  appStore.setLayoutHideHeader(false);
  setMenuSetting({ show: true });
  appStore.setLayoutHideMultiTabs(false);
  appStore.setLayoutHideSider(false);
});
let scene: THREE.Scene;
let camera: THREE.PerspectiveCamera;
let renderer: THREE.WebGLRenderer;
let light: THREE.DirectionalLight;
let controls: OrbitControls | null = null;
let gltf: THREE.Group | null = null; // 模型也用普通变量存储
onMounted(() => {
  if(!isCollapsed ){
    toggleCollapsed()
  }
  // 键盘事件监听
  document.addEventListener('keydown', function(event) {
    if (event.keyCode === 27) { // ESC键
      state.show = true
      setMenuSetting({ show: true });
      appStore.setLayoutHideHeader(true);
      appStore.setLayoutHideMultiTabs(true);
      appStore.setLayoutHideSider(true);
    }
  });

  // 初始化布局
  setMenuSetting({ show: true });
  appStore.setLayoutHideHeader(true);
  appStore.setLayoutHideMultiTabs(true);
  appStore.setLayoutHideSider(true);
  if (screenfull.isEnabled) {
    screenfull.on('change', () => {
      const isFull = screenfull.isFullscreen;
      state.show = !isFull; // 同步状态
      setCanvasHeight(isFull);
    });
  }
  // 窗口大小变化时重新调整canvas
  window.addEventListener('resize', () => {
    const isFull = !state.show;
    setCanvasHeight(isFull);
  });
  init()
  loadModel()
  animate()
});

function init() {
  // 初始化场景、相机、渲染器
  scene = new THREE.Scene();
  camera = new THREE.PerspectiveCamera(7.5, window.innerWidth / window.innerHeight, 1, 10000);
  renderer = new THREE.WebGLRenderer({ antialias: true });

  // 相机设置
  camera.position.z = 592.18919664 * 0.2;
  camera.position.x = -69.96514999 * 0.2;
  camera.position.y = 164.09902119 * 0.2;
  camera.up.set(0, 1, 0);
  camera.lookAt(new THREE.Vector3(0, 0, 0));

  // 渲染器设置
  renderer.setSize(window.innerWidth, window.innerHeight-60);
  const containerEl = container.value;
  if (containerEl) {
    containerEl.appendChild(renderer.domElement);
  }

  // 光源设置
  light = new THREE.DirectionalLight(0xffffff, 5);
  light.position.set(0, 1, 1);
  scene.add(light);

  // 控制器设置
  controls = new OrbitControls(camera, renderer.domElement);
  controls.addEventListener('change', () => {
    if (camera.position.z > 593) {
      camera.position.z = 592.18919664 * 0.2;
      camera.position.x = -69.96514999 * 0.9;
      camera.position.y = 164.09902119 * 0.9;
      controls!.enableZoom = true;
    } else {
      controls!.enableZoom = true;
    }
  });
  controls.maxAzimuthAngle = Math.PI / 10;
  controls.minAzimuthAngle = -Math.PI / 10;
  controls.maxPolarAngle = Math.PI / 2;
}
const container = ref(null);
// 每批加载的子对象数量
const BATCH_SIZE = 1;
// 每批加载的间隔时间(ms)
const BATCH_DELAY = 10;
function loadModel() {
  const loader = new GLTFLoader();
  var path = '../10000002.glb'
  loader.load(path, (loadedGltf) => {
    gltf = loadedGltf.scene;
    console.log('模型加载成功：', gltf);

    // 模型位置调整
    const box = new THREE.Box3().setFromObject(gltf);
    const mdlen = box.max.x - box.min.x;
    const mdwid = box.max.z - box.min.z;
    const mdhei = box.max.y - box.min.y;
    const x1 = box.min.x + mdlen / 2;
    const y1 = box.min.y + mdhei / 2;
    const z1 = box.min.z + mdwid / 2;
    gltf.position.set(-x1, -y1, -z1);
    if (path === '../10000002.glb') {
      gltf.position.set(-x1+ -3, -y1, -z1 + -10); // 将模型进行偏移
    }

    // 关键：添加到普通场景变量
    scene.add(gltf);
  }, undefined, (error) => {
    console.error('模型加载错误：', error);
  });
}
function animate() {
  requestAnimationFrame(animate);
  controls?.update(); // 控制器更新
  renderer.render(scene, camera); // 用普通变量渲染
}
// 组件卸载时清除定时器
onUnmounted(() => {

});

const go = useGo();
</script>

<style lang="less" scoped>
// 基础样式设置，确保页面占满屏幕且无滚动
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
  overflow: hidden; // 禁止页面滚动
}

::v-deep .amap-marker-label {
  padding: 0;
  border: 3px solid white;
}

.amap-container {
  background-image: none;
}

body {
  color: white;
  min-height: 100vh;
  width: 100vw;
}

// 容器占满整个屏幕
.fdq_container {
  //background-image: url("https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/fdq_bg.png");
  //background-size: cover;

  position: relative;
  z-index: 200;
  display: flex;
  flex-direction: column;
  min-height: 100vh;

}

/* 头部样式 - 固定高度 */
.header-panel {
  //background-image: url("https://yyjf-1304521166.cos.ap-chongqing.myqcloud.com/head.png");
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
  min-height: 280px;
  max-height: calc((100vh - 60px)/3);
  padding: 10px; // 增加内边距
  display: flex;
  flex-direction: column;
}

.map-container {
  flex: 2;
  //background: red;
  border-radius: 12px;
  //padding: 15px;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
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
  background: rgba(14, 38, 59, 0.3);
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
  //color: white;
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
  background: rgba(14, 38, 59, 0.35);
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
  //background:linear-gradient(90deg, rgba(14, 208, 242, 1)0%,rgba(255, 255, 255, 0) 100%);
  display: flex;
  align-items: center;
  margin-bottom: 5px;
  //color: white;
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
  //background: #66ffff;
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
  //color: white;
}

.task-user {
  font-size: 0.8rem;
  //color: white;
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
  color: white;
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
  padding: 20px;
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
  padding: 2px;
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

}

.statistic-value {
  font-size: 1.3rem;
  font-weight: bold;
  color: #00F0FF;

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
  min-height: 180px;
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
  //color: #00F0FF;
  margin-right: 10px;
  min-width: 50px;
}

.alarm-content {
  padding: 8px 5px;
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  //color: white;
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

.icon-white {
  background: rgba(47, 137, 252, 0);
  color: black;
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
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: white;
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
  background-color: white;
  color: #7dd3fc;
  transform: scale(1.08);
  box-shadow: 0 6px 25px rgba(255, 255, 255, 0.4);
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

.config-container {
  padding: 20px 10px;
}

.config-item {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.config-label {
  font-size: 14px;
  font-weight: 500;
}

.color-picker {
  width: 100%;
}

.select-control {
  width: 100%;
}

.slider-control {
  width: 100%;
}

.opacity-value {
  margin-left: 10px;
  font-size: 14px;
}

.config-actions {
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

// 确保卡片内文本颜色继承自父容器
.stat-card {
  color: inherit;

  .card-title {
    color: inherit;
  }

  .realtime-label,
  .maintenance-label,
  .statistic-label,
  .alarm-content {
    color: inherit;
  }

  // 步骤条样式调整
  ::v-deep .el-step__description {
    color: inherit !important;
    font-size: 0.8rem !important;
  }

  ::v-deep .el-step__title {
    color: inherit !important;
    font-size: 0.9rem !important;
  }

  ::v-deep .el-step__line{
    color: inherit
  }
}


</style>
