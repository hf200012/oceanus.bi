import request from '@/utils/request'

export function addDashboard(data) {
  return request({
    url: '/rest/bi/dashboard/create',
    method: 'POST',
    data
  })
}

export function updateDashboard(data) {
  return request({
    url: '/rest/bi/dashboard/update',
    method: 'put',
    data
  })
}

export function getdDashboardById(id) {
  return request({
    url: `/rest/bi/dashboard/query/${id}`
  })
}

export function deleteDashboard(data) {
  return request({
    url: `/rest/bi/dashboard/delete`,
    method: 'delete',
    params:data
  })
}

export function dashboardList(projectId) {
  return request({
    url: `/rest/bi/dashboard/list/${projectId}` 
  })
}

export function addChartToDB(data) {
  return request({
    url: '/rest/bi/dashboard/map',
    method: 'get',
    params:data
  })
}

export function chartByDashboard(id) {
  return request({
    url: `/rest/bi/dashboard/chartByDashboard/${id}`
  })
}

export function dbByChart(id) {
  return request({
    url: `/rest/bi/dashboard/boardbychart?chartId=${id}`
  })
}

export function unMapChartDb(data) {
  return request({
    url: '/rest/bi/dashboard/unmap',
    method: 'get',
    params:data
  })
}

export function dbOrder(data) {
  return request({
    url: '/rest/bi/dashboard/order',
    method: 'POST',
    data
  })
}


// 公共数据概览树
export function publicDashboardTree(id) {
  return request({
    url: `/rest/bi/dashboard/publictree/${id}`
  })
}

// 我的数据概览树
export function myDashboardTree(id) {
  return request({
    url: `/rest/bi/dashboard/mytree/${id}`
  })
}

//获取要分享的用户列表
export function getShareUserList() {
  return request({
    url: `/rest/share/list`
  })
}

//获取要分享的用户列表
export function saveShare(params) {
  return request({
    url: `/rest/share/add`,
    method: 'get',
    params:params
  })
}