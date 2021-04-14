// 兼容 IE
import 'core-js/stable'
import 'regenerator-runtime/runtime'

import '@/assets/style/index.scss' // global css
import '@/assets/style/venus.scss'
import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'

import './assets/style/element-variables.scss'

import 'element-ui/lib/theme-chalk/index.css'

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import './icons'

// 全局引入按需引入UI库 vant
import '@/plugins/vant'
// 引入全局样式
import '@/assets/css/index.scss'
import '@/assets/style/iconfont.css'
// 移动端适配
import 'lib-flexible/flexible.js'

import permission from './directive/permission'
import Cookies from 'js-cookie'
import Pagination from "@/components/Pagination";

Vue.component('Pagination', Pagination)

import './permission' 

import i18n from './i18n'

// filters
import './filters'
Vue.config.productionTip = false

Vue.use(permission)

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
