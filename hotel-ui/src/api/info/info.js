import request from '@/utils/request'

// 查询客房信息列表
export function listInfo(query) {
  return request({
    url: '/info/info/list',
    method: 'get',
    params: query
  })
}

// 查询客房信息列表（包含房间类型信息）
export function listInfoWithTypeInfo(query) {
  return request({
    url: '/info/info/listWithTypeInfo',
    method: 'get',
    params: query
  })
}

// 查询客房信息详细
export function getInfo(roomId) {
  return request({
    url: '/info/info/' + roomId,
    method: 'get'
  })
}

// 新增客房信息
export function addInfo(data) {
  return request({
    url: '/info/info',
    method: 'post',
    data: data
  })
}

// 修改客房信息
export function updateInfo(data) {
  return request({
    url: '/info/info',
    method: 'put',
    data: data
  })
}

// 删除客房信息
export function delInfo(roomId) {
  return request({
    url: '/info/info/' + roomId,
    method: 'delete'
  })
}

// 下载导入模板
export function importTemplate() {
  return request({
    url: '/info/info/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}

// 导入客房信息数据
export function importData(data) {
  return request({
    url: '/info/info/importData',
    method: 'post',
    data: data
  })
}

// 导出客房信息列表（包含房间类型信息）
export function exportWithTypeInfo(query) {
  return request({
    url: '/info/info/exportWithTypeInfo',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}