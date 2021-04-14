import Vue from 'vue'

import Cookies from 'js-cookie'
import '@/assets/styles/index.scss' // global css
import '@/assets/styles/venus.scss'
import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import permission from './directive/permission'

import './assets/styles/element-variables.scss'

import 'element-ui/lib/theme-chalk/index.css'
import App from './App'
import store from './store'
import router from './router'
import echarts from "echarts";

import './permission' // permission control

import vueParticles from 'vue-particles'
import Vcomp from './components/index'
import Toast from './components/toast'


Vue.use(vueParticles)
Vue.use(Vcomp)
Vue.use(echarts)


Vue.prototype.$Toast = Toast
Vue.prototype.echarts = echarts

import i18n from './i18n'

import './icons'

Vue.config.productionTip = false


import { getDicts } from "@/api/data";
import { getConfigKey } from "@/api/config";
import { parseTime, resetForm, addDateRange,selectDictLabel,download} from "@/utils/venus";
import Pagination from "@/components/Pagination";


// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.parseTime = parseTime
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.download = download


Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}

// 全局组件挂载
Vue.component('Pagination', Pagination)
Vue.use(permission)

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */
store.commit('app/SET_LANG', 'CN')

Vue.use(Element, {
  i18n: (key, value) => i18n.t(key, value)
})

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})


new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})
