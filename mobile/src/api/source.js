import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/venus";


//同步元数据
export function syncMetadata(id) {
  return request({
    url: '/rest/bi/datasource/sync/'+ praseStrEmpty(id) ,
    method: 'get'
  })
}

// 新增数据源
export function addDatasource(data) {
  return request({
    url: '/rest/bi/datasource/add',
    method: 'post',
    data: data
  })
}
// 获取数据源列表信息
export function getDatasourceList(query) {
  return request({
    url: '/rest/bi/datasource/list',
    method: 'get',
    params: query
  })
}
// 修改数据源
export function updateDatasource(data) {
  return request({
    url: '/rest/bi/datasource/edit',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function delDatasource(id) {
  return request({
    url: '/rest/bi/datasource/delete/'+ praseStrEmpty(id) ,
    method: 'delete'
  })
}

export function getDatasource(id) {
  return request({
    url: '/rest/bi/datasource/query/' + praseStrEmpty(id),
    method: 'get'
  })
}


// 查询表格列表
export function tabletreeselect() {
  return request({
    url: '/rest/bi/datasource/metatree',
    method: 'get'
  })
}

//查询表字段列表
export function getTableFieldList(query) {
  return request({
    url: '/rest/bi/datasource/fields',
    method: 'get',
    params:query
  })
}


//查询表字段列表
export function getTableFieldListById(query) {
  return request({
    url: '/rest/bi/datasource/fieldsById',
    method: 'get',
    params:query
  })
}

//根据数据源编号查询该数据源的所有表
export function tablesByBase(id) {
  return request({
    url: '/rest/bi/datasource/getTable/'+ praseStrEmpty(id),
    method: 'get'
  })
}
//保存表的中文名称
export function saveTable(params) {
  return request({
    url: '/rest/bi/datasource/saveTableConfig',
    method: 'POST',
    data:params
  })
}

//修改表的字段中文名称
export function saveTableFields(params) {
  return request({
    url: '/rest/bi/meta/saveTableFieldConfig',
    method: 'POST',
    data:params
  })
}
//数据源添加表
export function addTable(data) {
  return request({
    url: '/rest/bi/meta/addtable',
    method: 'POST',
    data
  })
}


// 下载表字段导入模板
export function importTemplate() {
  return request({
    url: '/rest/bi/meta/dowloadTemplate',
    method: 'get'
  })
}
