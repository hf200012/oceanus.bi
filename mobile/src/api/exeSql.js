import request from '@/utils/request'
import axios from 'axios'

export default function() {
  const source = axios.CancelToken.source()
  return {
    cancel() {
      source.cancel('cancel')
    },
    fetch(data) {
      return request({
        url: '/rest/bi/exec/exec',
        method: 'POST',
        data
      })
    }
  }
}


export function exec(data) {
  return request({
    url: '/rest/bi/exec/exec',
    method: 'POST',
    data
  })
}
