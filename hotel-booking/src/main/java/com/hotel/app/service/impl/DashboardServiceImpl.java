package com.hotel.app.service.impl;

import com.hotel.app.domain.ReservationInfo;
import com.hotel.app.domain.dto.ReservationInfoDTO;
import com.hotel.app.mapper.CustomerInfoMapper;
import com.hotel.app.mapper.ReservationInfoMapper;
import com.hotel.app.mapper.RoomInfoMapper;
import com.hotel.app.mapper.RoomTypeMapper;
import com.hotel.app.service.DashboardService;
import com.hotel.app.service.IReservationInfoService;
import com.hotel.app.service.IRoomInfoService;
import com.hotel.app.service.IRoomTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仪表板服务实现类
 * 
 * @author ruoyi
 * @date 2025-09-02
 */
@Service
public class DashboardServiceImpl implements DashboardService {
    private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

    @Autowired
    private IReservationInfoService reservationInfoService;

    @Autowired
    private IRoomInfoService roomInfoService;

    @Autowired
    private IRoomTypeService roomTypeService;

    @Autowired
    private ReservationInfoMapper reservationInfoMapper;

    @Autowired
    private RoomInfoMapper roomInfoMapper;
    
    @Autowired
    private RoomTypeMapper roomTypeMapper;
    
    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    /**
     * 获取近7天预约统计数据
     * 
     * @return 预约统计数据
     */
    @Override
    public Map<String, Object> getRecentWeekReservations() {
        logger.info("开始获取近7天预约统计数据");
        Map<String, Object> result = new HashMap<>();
        
        // 获取近7天的日期
        List<String> dates = new ArrayList<>();
        List<Integer> reservationCounts = new ArrayList<>();
        List<BigDecimal> revenueAmounts = new ArrayList<>();
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // 星期名称映射
        Map<String, String> weekMap = new HashMap<>();
        weekMap.put("MONDAY", "周一");
        weekMap.put("TUESDAY", "周二");
        weekMap.put("WEDNESDAY", "周三");
        weekMap.put("THURSDAY", "周四");
        weekMap.put("FRIDAY", "周五");
        weekMap.put("SATURDAY", "周六");
        weekMap.put("SUNDAY", "周日");
        
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String weekDay = date.getDayOfWeek().toString();
            String chineseWeekDay = weekMap.get(weekDay);
            // 格式：MM-dd(周X)
            dates.add(date.format(DateTimeFormatter.ofPattern("MM-dd")) + "(" + chineseWeekDay + ")");
            
            // 查询该日期的预订数量
            int count = reservationInfoMapper.selectReservationCountByDate(java.sql.Date.valueOf(date));
            reservationCounts.add(count);
            
            // 查询该日期的收入金额
            BigDecimal revenue = reservationInfoMapper.selectRevenueByDate(java.sql.Date.valueOf(date));
            revenueAmounts.add(revenue);
        }
        
        result.put("dates", dates);
        result.put("reservationCounts", reservationCounts);
        result.put("revenueAmounts", revenueAmounts);
        
        logger.info("近7天预约统计数据获取完成，日期数量: {}, 预订数数量: {}, 收入数量: {}", 
                   dates.size(), reservationCounts.size(), revenueAmounts.size());
        return result;
    }

    /**
     * 获取客房类型预订统计（用于词云显示）
     * 
     * @return 客房类型预订统计
     */
    @Override
    public List<Map<String, Object>> getRoomTypeReservations() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 查询所有客房类型及其预订数量
        List<Map<String, Object>> roomTypeReservations = reservationInfoMapper.selectRoomTypeReservationCount();
        
        for (Map<String, Object> item : roomTypeReservations) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", item.get("typeName"));
            map.put("value", item.get("reservationCount"));
            result.add(map);
        }
        
        return result;
    }

    /**
     * 获取客房状态分布数据
     * 
     * @return 客房状态分布数据
     */
    @Override
    public Map<String, Integer> getRoomStatusDistribution() {
        Map<String, Integer> result = new HashMap<>();
        
        // 查询各种状态的客房数量
        int freeCount = roomInfoMapper.selectRoomCountByStatus("0"); // 空闲
        int reservedCount = roomInfoMapper.selectRoomCountByStatus("1"); // 已预定
        int occupiedCount = roomInfoMapper.selectRoomCountByStatus("2"); // 已入住
        int maintenanceCount = roomInfoMapper.selectRoomCountByStatus("3"); // 维修中
        
        result.put("空闲", freeCount);
        result.put("已预定", reservedCount);
        result.put("已入住", occupiedCount);
        result.put("维修中", maintenanceCount);
        
        return result;
    }

    /**
     * 获取预订状态统计
     * 
     * @return 预订状态统计
     */
    @Override
    public Map<String, Integer> getReservationStatusStatistics() {
        Map<String, Integer> result = new HashMap<>();
        
        // 查询各种预订状态的数量
        int reservedCount = reservationInfoMapper.selectReservationCountByStatus("0"); // 已预定
        int confirmedCount = reservationInfoMapper.selectReservationCountByStatus("1"); // 已确认
        int checkedInCount = reservationInfoMapper.selectReservationCountByStatus("2"); // 已入住
        int cancelledCount = reservationInfoMapper.selectReservationCountByStatus("3"); // 已取消
        int completedCount = reservationInfoMapper.selectReservationCountByStatus("4"); // 已完成
        
        result.put("已预定", reservedCount);
        result.put("已确认", confirmedCount);
        result.put("已入住", checkedInCount);
        result.put("已取消", cancelledCount);
        result.put("已完成", completedCount);
        
        return result;
    }

    /**
     * 获取客房类型分布数据
     * 
     * @return 客房类型分布数据
     */
    @Override
    public Map<String, Integer> getRoomTypeDistribution() {
        Map<String, Integer> result = new HashMap<>();
        
        // 查询各种客房类型的数量
        List<Map<String, Object>> roomTypeCounts = roomTypeMapper.selectRoomTypeCount();
        
        for (Map<String, Object> item : roomTypeCounts) {
            result.put((String) item.get("typeName"), ((Long) item.get("count")).intValue());
        }
        
        return result;
    }

    /**
     * 获取月度收入趋势数据
     * 
     * @return 月度收入趋势数据
     */
    @Override
    public Map<String, Object> getMonthlyRevenue() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取最近12个月的月份
        List<String> months = new ArrayList<>();
        List<BigDecimal> revenues = new ArrayList<>();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        
        for (int i = 11; i >= 0; i--) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -i);
            months.add(sdf.format(cal.getTime()));
            
            // 查询该月份的收入
            BigDecimal revenue = reservationInfoMapper.selectRevenueByMonth(cal.getTime());
            revenues.add(revenue);
        }
        
        result.put("months", months);
        result.put("revenues", revenues);
        
        return result;
    }

    /**
     * 获取近期预订记录
     * 
     * @return 近期预订记录
     */
    @Override
    public List<ReservationInfoDTO> getRecentReservations() {
        // 查询最近10条预订记录
        ReservationInfo query = new ReservationInfo();
        List<ReservationInfoDTO> reservations = reservationInfoService.selectReservationInfoDTOList(query);
        
        // 按创建时间倒序排列，取前10条
        if (reservations != null) {
            return reservations.stream()
                    .sorted((r1, r2) -> {
                        if (r1.getCreateTime() == null || r2.getCreateTime() == null) {
                            return 0;
                        }
                        return r2.getCreateTime().compareTo(r1.getCreateTime());
                    })
                    .limit(10)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    
    /**
     * 获取近7天的预订记录
     * 
     * @return 近7天的预订记录
     */
    @Override
    public List<ReservationInfoDTO> getRecentWeekReservationsList() {
        List<ReservationInfoDTO> reservations = reservationInfoMapper.selectRecentWeekReservations();
        
        // 按创建时间倒序排列，取前10条
        if (reservations != null) {
            return reservations.stream()
                    .sorted((r1, r2) -> {
                        if (r1.getCreateTime() == null || r2.getCreateTime() == null) {
                            return 0;
                        }
                        return r2.getCreateTime().compareTo(r1.getCreateTime());
                    })
                    .limit(10)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    
    /**
     * 获取总客户数
     * 
     * @return 总客户数
     */
    @Override
    public int getTotalCustomers() {
        int count = customerInfoMapper.selectTotalCustomerCount();
        logger.info("获取总客户数: {}", count);
        return count;
    }
    
    /**
     * 获取总预订数
     * 
     * @return 总预订数
     */
    @Override
    public int getTotalReservations() {
        int count = reservationInfoMapper.selectTotalReservationCount();
        logger.info("获取总预订数: {}", count);
        return count;
    }
    
    /**
     * 获取总收入
     * 
     * @return 总收入
     */
    @Override
    public double getTotalRevenue() {
        BigDecimal revenue = reservationInfoMapper.selectTotalRevenue();
        double revenueValue = revenue != null ? revenue.doubleValue() : 0.0;
        logger.info("获取总收入: {}", revenueValue);
        return revenueValue;
    }
    
    /**
     * 获取总房间数
     * 
     * @return 总房间数
     */
    @Override
    public int getTotalRooms() {
        int count = roomInfoMapper.selectTotalRoomCount();
        logger.info("获取总房间数: {}", count);
        return count;
    }
}