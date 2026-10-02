import request from '@/utils/request'

// 查询客户信息列表
export function listCustomerInfo(query) {
  return request({
    url: '/customerInfo/customerInfo/list',
    method: 'get',
    params: query
  })
}

// 查询客户信息详细
export function getCustomerInfo(customerId) {
  return request({
    url: '/customerInfo/customerInfo/' + customerId,
    method: 'get'
  })
}

// 新增客户信息
export function addCustomerInfo(data) {
  return request({
    url: '/customerInfo/customerInfo',
    method: 'post',
    data: data
  })
}

// 修改客户信息
export function updateCustomerInfo(data) {
  return request({
    url: '/customerInfo/customerInfo',
    method: 'put',
    data: data
  })
}

// 删除客户信息
export function delCustomerInfo(customerId) {
  return request({
    url: '/customerInfo/customerInfo/' + customerId,
    method: 'delete'
  })
}

// 导出客户信息
export function exportCustomerInfo(query) {
  return request({
    url: '/customerInfo/customerInfo/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 下载客户信息导入模板
export function importTemplate() {
  return request({
    url: '/customerInfo/customerInfo/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}