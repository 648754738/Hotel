import request from '@/utils/request'

// 查询客房类型列表
export function listType(query) {
  return request({
    url: '/type/type/list',
    method: 'get',
    params: query
  })
}

// 查询客房类型详细
export function getType(typeId) {
  return request({
    url: '/type/type/' + typeId,
    method: 'get'
  })
}

// 新增客房类型
export function addType(data) {
  return request({
    url: '/type/type',
    method: 'post',
    data: data
  })
}

// 修改客房类型
export function updateType(data) {
  return request({
    url: '/type/type',
    method: 'put',
    data: data
  })
}

// 删除客房类型
export function delType(typeId) {
  return request({
    url: '/type/type/' + typeId,
    method: 'delete'
  })
}
