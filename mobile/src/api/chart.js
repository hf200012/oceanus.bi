import request from '@/utils/request'

export function createChart(data) {
  return request({
    url: '/rest/bi/chart/create',
    method: 'POST',
    data
  })
}

export function updateChart(data) {
  return request({
    url: '/rest/bi/chart/update',
    method: 'PUT',
    data: data
  })
}

export function getChartById(id) {
  return request({
    url: `/rest/bi/chart/tables/${id}`
  })
}

export function deleteChart(data) {
  return request({
    url: `/rest/bi/chart/delete`,
    method: 'delete',
    params:data
  })
}

export function chartList(params) {
  return request({
    url: `/rest/bi/chart/list`,
    method:'get',
    params:params
  })
}
