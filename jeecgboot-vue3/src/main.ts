import type { MainAppProps } from "#/main";
import 'uno.css';
import '/@/design/index.less';
import 'ant-design-vue/dist/reset.css';
// 注册图标
import 'virtual:svg-icons-register';
import App from './App.vue';
import { createApp } from 'vue';
import { initAppConfigStore } from '/@/logics/initAppConfig';
import { setupErrorHandle } from '/@/logics/error-handle';
import { router, createRouter, setupRouter } from '/@/router';
import { setupRouterGuard } from '/@/router/guard';
import { setupStore } from '/@/store';
import { setupGlobDirectives } from '/@/directives';
import { setupI18n } from '/@/locales/setupI18n';
import { registerGlobComp } from '/@/components/registerGlobComp';
import { registerThirdComp } from '/@/settings/registerThirdComp';
import { registerSuper } from '/@/views/super/registerSuper';
import { useSso } from '/@/hooks/web/useSso';
import { checkIsQiankunMicro } from "/@/qiankun/micro";
import { autoUseQiankunMicro } from "/@/qiankun/micro/qiankunMicro";
import { useAppStoreWithOut } from "@/store/modules/app";
import dataV from '@jiaminghi/data-view'
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
// 注册online模块lib
import { registerPackages } from '/@/utils/monorepo/registerPackages';

// 程序入口
async function main() {
  if (checkIsQiankunMicro()) {
    // 【JEECG作为乾坤子应用】以乾坤子应用模式启动
    // await autoUseQiankunMicro(bootstrap)
    await autoUseQiankunMicro(bootstrap)
  } else {
    // 获取参数
    const props = getMainAppProps();
    // 普通启动
    await bootstrap(props)
  }
}

main();

async function bootstrap(props?: MainAppProps) {
  // 创建应用实例
  const app = createApp(App);
  app.use(dataV).use(ElementPlus);
  // 【QQYUN-6329】
  window['JAppRootInstance'] = app;

  // 创建路由
  createRouter();

  // 配置存储
  setupStore(app);

  // 配置参数
  setupProps(props);

  // 多语言配置,异步情况:语言文件可以从服务器端获得
  await setupI18n(app);

  // 初始化内部系统配置
  initAppConfigStore();

  // 注册外部模块路由(注册online模块lib)
  registerPackages(app);

  // 注册全局组件
  registerGlobComp(app);

  //CAS单点登录
  await useSso().ssoLogin();

  // 注册super应用路由
  await registerSuper(app);

  // 配置路由
  setupRouter(app);

  // 路由保护
  setupRouterGuard(router);

  // 注册全局指令
  setupGlobDirectives(app);

  // 配置全局错误处理
  setupErrorHandle(app);

  // 注册第三方组件
  await registerThirdComp(app);

  // 当路由准备好时再执行挂载( https://next.router.vuejs.org/api/#isready)
  await router.isReady();

  // 挂载应用
  app.mount(getMountContainer(props), true);

  console.log(" vue3 app 加载完成！")

  return app
}

// 获取应用挂载容器
function getMountContainer(props?: MainAppProps) {
  const id = '#app';
  if (!props?.container?.querySelector) {
    return id;
  }
  return props.container.querySelector(id) ?? id;
}

// 获取主应用参数
function getMainAppProps(): MainAppProps {
  // 从 queryString 中获取
  const searchParams = new URLSearchParams(window.location.search);
  // 隐藏侧边栏（菜单）
  let hideSider = searchParams.get('hideSider') === 'true';
  // 隐藏顶部
  let hideHeader = searchParams.get('hideHeader') === 'true';
  // 隐藏 多Tab 切换
  let hideMultiTabs = searchParams.get('hideMultiTabs') === 'true';

  return {
    hideSider,
    hideHeader,
    hideMultiTabs
  }
}

// 配置主应用参数
function setupProps(props?: MainAppProps) {
  if (!props) {
    return
  }
  const appStore = useAppStoreWithOut();
  appStore.setMainAppProps(props);
}
