package com.hotel.app.service;

import com.hotel.app.domain.dto.ReservationInfoDTO;

import java.util.List;
import java.util.Map;

/**
 * 仪表板服务接口
 * 
 * @author ruoyi
 * @date 2025-09-02
 */
public interface DashboardService {
    /**
     * 获取近7天预约统计数据
     * 
     * @return 预约统计数据
     */
    public Map<String, Object> getRecentWeekReservations();

    /**
     * 获取客房类型预订统计（用于词云显示）
     * 
     * @return 客房类型预订统计
     */
    public List<Map<String, Object>> getRoomTypeReservations();

    /**
     * 获取客房状态分布数据
     * 
     * @return 客房状态分布数据
     */
    public Map<String, Integer> getRoomStatusDistribution();

    /**
     * 获取预订状态统计
     * 
     * @return 预订状态统计
     */
    public Map<String, Integer> getReservationStatusStatistics();

    /**
     * 获取客房类型分布数据
     * 
     * @return 客房类型分布数据
     */
    public Map<String, Integer> getRoomTypeDistribution();

    /**
     * 获取月度收入趋势数据
     * 
     * @return 月度收入趋势数据
     */
    public Map<String, Object> getMonthlyRevenue();

    /**
     * 获取近期预订记录
     * 
     * @return 近期预订记录
     */
    public List<ReservationInfoDTO> getRecentReservations();
    
    /**
     * 获取近7天的预订记录
     * 
     * @return 近7天的预订记录
     */
    public List<ReservationInfoDTO> getRecentWeekReservationsList();
    
    /**
     * 获取总客户数
     * 
     * @return 总客户数
     */
    public int getTotalCustomers();
    
    /**
     * 获取总预订数
     * 
     * @return 总预订数
     */
    public int getTotalReservations();
    
    /**
     * 获取总收入
     * 
     * @return 总收入
     */
    public double getTotalRevenue();
    
    /**
     * 获取总房间数
     * 
     * @return 总房间数
     */
    public int getTotalRooms();
}