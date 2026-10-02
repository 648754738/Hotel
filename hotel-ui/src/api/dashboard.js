import request from '@/utils/request'

// 获取统计数据
export function getStats() {
  return request({
    url: '/dashboard/stats',
    method: 'get'
  })
}

// 获取客房状态分布数据
export function getRoomStatus() {
  return request({
    url: '/dashboard/roomStatus',
    method: 'get'
  })
}

// 获取预订状态统计
export function getReservationStatus() {
  return request({
    url: '/dashboard/reservationStatus',
    method: 'get'
  })
}

// 获取客房类型分布
export function getRoomType() {
  return request({
    url: '/dashboard/roomType',
    method: 'get'
  })
}

// 获取月度收入趋势
export function getMonthlyRevenue() {
  return request({
    url: '/dashboard/monthlyRevenue',
    method: 'get'
  })
}

// 获取近期预订记录
export function getRecentReservations() {
  return request({
    url: '/dashboard/recentReservations',
    method: 'get'
  })
}

// 获取近7天的预订记录
export function getRecentWeekReservationsList() {
  return request({
    url: '/dashboard/recentWeekReservationsList',
    method: 'get'
  })
}

// 获取近一周房间预订统计数据
export function getRecentWeekReservations() {
  return request({
    url: '/dashboard/recentWeekReservations',
    method: 'get'
  })
}

// 获取客房类型预订统计（用于词云显示）
export function getRoomTypeReservations() {
  return request({
    url: '/dashboard/roomTypeReservations',
    method: 'get'
  })
}