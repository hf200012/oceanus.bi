import axios from 'axios'
import { Message } from 'element-ui'
import Cookies from 'js-cookie'
import store from '../store'

const fetchInstance = axios.create({
  baseURL: process.env.VUE_APP_BASE_API
  // timeout: 2000,
})

// request拦截器
fetchInstance.interceptors.request.use(config => {
  if (store.getters.token) {
    config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
  }
  config.withCredentials = true
  return config
}, error => {
  console.log(error) // for debug
  Promise.reject(error)
})

// response拦截器
fetchInstance.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      Message({
        message: res.message,
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject('request error')
    } else {
      return response.data
    }
  },
  error => {
    if (error.message !== 'cancel') {
      console.log('err:' + error)// for debug
      Message({
        message: error.message,
        type: 'error',
        duration: 5 * 1000
      })
    }

    // Raven.captureException(error)
    return Promise.reject(error)
  }
)

export default fetchInstance
