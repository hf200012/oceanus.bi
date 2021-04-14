import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/venus";


// 获取项目列表信息
export function getProjectList() {
  return request({
    url: '/rest/project/list',
    method: 'get'
  })
}


// 新增项目
export function addProject(data) {
  return request({
    url: '/rest/project/create',
    method: 'post',
    data
  })
}

// 编辑项目
export function editProject(data) {
  return request({
    url: '/rest/project/update',
    method: 'put',
    data
  })
}


// 编辑项目
export function getProject(id) {
  return request({
    url: `/rest/project/query/${id}`,
    method: 'get',
  })
}

// 删除项目
export function deleteProject(id) {
  return request({
    url: `/rest/project/delete/${id}` ,
    method: 'delete'
  })
}