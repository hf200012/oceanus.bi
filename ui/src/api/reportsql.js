import request from '@/utils/request'


//获取sql执行结果
export function getReportsqlResultList(data) {
  return request({
    url: '/rest/report/sql_exec',
    method: 'post',
    data
  })
}

//根据报表编号删除报表
export function deleteReportByid(id) {
  return request({
    url: `/rest/report/remove/${id}`,
    method: 'delete'
  })
}



//根据报表编号及数据源编号获取报表数据
export function reportList(data) {
  return request({
    url: '/rest/report/reportresult',
    method: 'get',
    params:data
  })
}


//获取数据源
export function getReportds() {
  return request({
    url: '/rest/bi/datasource/list',
    method: 'get'
  })
}


// 查询数据源对应表格列表
export function getReportTreeTableselect(params) {
  return request({
    url: '/rest/bi/datasource/tabletree/' + params,
    method: 'get'
  })
}
//更新报表
export function updateReport(data){
  return request({
    url: '/rest/report/update',
    method: 'put',
    data
  })
}

//新建报表
export function createReport(data){
  return request({
    url: '/rest/report/create',
    method: 'post',
    data
  })
}


//新建报表
export function getReport(id){
  return request({
    url: '/rest/report/query/' +id,
    method: 'get'
  })
}

// 公共数据报表
export function publicReportTree() {
  return request({
    url: `/rest/report/publictree`
  })
}

// 我的数据报表
export function myReportTree() {
  return request({
    url: `/rest/report/myreporttree/`
  })
}

//保存分享
export function saveShare(params) {
  return request({
    url: `/rest/report/share`,
    method: 'get',
    params:params
  })
}

//保存分享
export function reportALlList(params) {
  return request({
    url: `/rest/report/list`,
    method: 'get',
    params:params
  })
}

