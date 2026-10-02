package com.hotel.app.controller;

import com.hotel.app.domain.dto.ReservationInfoDTO;
import com.hotel.app.service.DashboardService;
import com.hotel.app.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 仪表板控制器
 * 
 * @author ruoyi
 * @date 2025-09-02
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取统计数据
     * 
     * @return 统计数据
     */
    @GetMapping("/stats")
    public AjaxResult getStats() {
        try {
            // 获取统计数据
            int totalCustomers = dashboardService.getTotalCustomers();
            int totalReservations = dashboardService.getTotalReservations();
            double totalRevenue = dashboardService.getTotalRevenue();
            int totalRooms = dashboardService.getTotalRooms();
            
            // 构造返回数据

            Map<String, Object> data = new java.util.HashMap<>();
            data.put("customerCount", totalCustomers);
            data.put("reservationCount", totalReservations);
            data.put("totalRevenue", totalRevenue);
            data.put("roomCount", totalRooms);
            
            return AjaxResult.success("统计数据获取成功", data);
        } catch (Exception e) {
            return AjaxResult.error("统计数据获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取客房状态分布数据
     * 
     * @return 客房状态分布数据
     */
    @GetMapping("/roomStatus")
    public AjaxResult getRoomStatus() {
        Map<String, Integer> roomStatusData = dashboardService.getRoomStatusDistribution();
        return AjaxResult.success(roomStatusData);
    }

    /**
     * 获取预订状态统计
     * 
     * @return 预订状态统计
     */
    @GetMapping("/reservationStatus")
    public AjaxResult getReservationStatus() {
        Map<String, Integer> reservationStatusData = dashboardService.getReservationStatusStatistics();
        return AjaxResult.success(reservationStatusData);
    }

    /**
     * 获取客房类型分布
     * 
     * @return 客房类型分布
     */
    @GetMapping("/roomType")
    public AjaxResult getRoomType() {
        Map<String, Integer> roomTypeData = dashboardService.getRoomTypeDistribution();
        return AjaxResult.success(roomTypeData);
    }

    /**
     * 获取月度收入趋势
     * 
     * @return 月度收入趋势
     */
    @GetMapping("/monthlyRevenue")
    public AjaxResult getMonthlyRevenue() {
        Map<String, Object> monthlyRevenueData = dashboardService.getMonthlyRevenue();
        return AjaxResult.success(monthlyRevenueData);
    }

    /**
     * 获取近期预订记录
     * 
     * @return 近期预订记录
     */
    @GetMapping("/recentReservations")
    public AjaxResult getRecentReservations() {
        List<ReservationInfoDTO> recentReservations = dashboardService.getRecentReservations();
        return AjaxResult.success(recentReservations);
    }

    /**
     * 获取近7天的预订记录
     * 
     * @return 近7天的预订记录
     */
    @GetMapping("/recentWeekReservationsList")
    public AjaxResult getRecentWeekReservationsList() {
        List<ReservationInfoDTO> recentWeekReservations = dashboardService.getRecentWeekReservationsList();
        return AjaxResult.success(recentWeekReservations);
    }

    /**
     * 获取近一周房间预订统计数据
     * 
     * @return 近一周房间预订统计数据
     */
    @GetMapping("/recentWeekReservations")
    public AjaxResult getRecentWeekReservations() {
        Map<String, Object> recentWeekReservations = dashboardService.getRecentWeekReservations();
        return AjaxResult.success(recentWeekReservations);
    }

    /**
     * 获取客房类型预订统计（用于词云显示）
     * 
     * @return 客房类型预订统计
     */
    @GetMapping("/roomTypeReservations")
    public AjaxResult getRoomTypeReservations() {
        List<Map<String, Object>> roomTypeReservations = dashboardService.getRoomTypeReservations();
        return AjaxResult.success(roomTypeReservations);
    }
}