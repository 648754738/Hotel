import request from '@/utils/request'

// 查询预定信息列表
export function listReservationInfo(query) {
  return request({
    url: '/reservationInfo/reservationInfo/list',
    method: 'get',
    params: query
  })
}

// 查询预定信息详细
export function getReservationInfo(reservationId) {
  return request({
    url: '/reservationInfo/reservationInfo/' + reservationId,
    method: 'get'
  })
}

// 新增预定信息
export function addReservationInfo(data) {
  return request({
    url: '/reservationInfo/reservationInfo',
    method: 'post',
    data: data
  })
}

// 修改预定信息
export function updateReservationInfo(data) {
  return request({
    url: '/reservationInfo/reservationInfo',
    method: 'put',
    data: data
  })
}

// 删除预定信息
export function delReservationInfo(reservationId) {
  return request({
    url: '/reservationInfo/reservationInfo/' + reservationId,
    method: 'delete'
  })
}

// 查询空闲房间列表
export function listFreeRooms(query) {
  return request({
    url: '/info/info/list',
    method: 'get',
    params: { ...query, roomStatus: '0' } // 0表示空闲状态
  })
}

// 查询客户信息列表
export function listCustomers(query) {
  return request({
    url: '/customerInfo/customerInfo/list',
    method: 'get',
    params: query
  })
}