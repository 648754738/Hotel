package com.hotel.app.service;

import com.hotel.app.domain.ReservationInfo;
import com.hotel.app.domain.dto.ReservationInfoDTO;
import java.util.List;

/**
 * 预定信息Service接口
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
public interface IReservationInfoService {
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
     * 批量删除预定信息
     * 
     * @param reservationIds 需要删除的预定信息ID
     * @return 结果
     */
    public int deleteReservationInfoByIds(Long[] reservationIds);

    /**
     * 删除预定信息信息
     * 
     * @param reservationId 预定信息ID
     * @return 结果
     */
    public int deleteReservationInfoById(Long reservationId);
}