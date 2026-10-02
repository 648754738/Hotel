package com.hotel.app.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.hotel.app.domain.ReservationInfo;
import com.hotel.app.domain.dto.ReservationInfoDTO;

/**
 * 预定信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
public interface ReservationInfoMapper {
    /**
     * 查询预定信息
     * 
     * @param reservationId 预定信息ID
     * @return 预定信息
     */
    public ReservationInfo selectReservationInfoById(Long reservationId);

    /**
     * 查询预定信息列表
     * 
     * @param reservationInfo 预定信息
     * @return 预定信息集合
     */
    public List<ReservationInfo> selectReservationInfoList(ReservationInfo reservationInfo);

    /**
     * 查询预定信息列表（包含客户和房间信息）
     *
     * @param reservationInfo 预定信息
     * @return 预定信息DTO集合
     */
    public List<ReservationInfoDTO> selectReservationInfoDTOList(ReservationInfo reservationInfo);

    /**
     * 新增预定信息
     * 
     * @param reservationInfo 预定信息
     * @return 结果
     */
    public int insertReservationInfo(ReservationInfo reservationInfo);

    /**
     * 修改预定信息
     * 
     * @param reservationInfo 预定信息
     * @return 结果
     */
    public int updateReservationInfo(ReservationInfo reservationInfo);

    /**
     * 删除预定信息
     * 
     * @param reservationId 预定信息ID
     * @return 结果
     */
    public int deleteReservationInfoById(Long reservationId);

    /**
     * 批量删除预定信息
     * 
     * @param reservationIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteReservationInfoByIds(Long[] reservationIds);
    
    /**
     * 根据日期查询预订数量
     * 
     * @param date 日期
     * @return 预订数量
     */
    public int selectReservationCountByDate(java.sql.Date date);
    
    /**
     * 根据日期查询收入金额
     * 
     * @param date 日期
     * @return 收入金额
     */
    public BigDecimal selectRevenueByDate(java.sql.Date date);
    
    /**
     * 查询客房类型预订统计
     * 
     * @return 客房类型预订统计
     */
    public List<Map<String, Object>> selectRoomTypeReservationCount();
    
    /**
     * 根据状态查询预订数量
     * 
     * @param status 状态
     * @return 预订数量
     */
    public int selectReservationCountByStatus(String status);
    
    /**
     * 根据月份查询收入金额
     * 
     * @param date 月份日期
     * @return 收入金额
     */
    public BigDecimal selectRevenueByMonth(Date date);
    
    /**
     * 查询总客户数
     * 
     * @return 总客户数
     */
    public int selectTotalCustomerCount();
    
    /**
     * 查询总预订数
     * 
     * @return 总预订数
     */
    public int selectTotalReservationCount();
    
    /**
     * 查询总收入
     * 
     * @return 总收入
     */
    public BigDecimal selectTotalRevenue();
    
    /**
     * 查询近7天的预订记录
     * 
     * @return 近7天的预订记录
     */
    public List<ReservationInfoDTO> selectRecentWeekReservations();
}