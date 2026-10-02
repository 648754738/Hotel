package com.hotel.app.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.zxing.WriterException;
import com.hotel.app.common.utils.QRCodeUtils;
import com.hotel.app.domain.RoomInfo;
import com.hotel.app.domain.dto.ReservationInfoDTO;
import com.hotel.app.service.IRoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotel.app.domain.ReservationInfo;
import com.hotel.app.mapper.ReservationInfoMapper;
import com.hotel.app.service.IReservationInfoService;
import com.hotel.app.common.utils.SecurityUtils;

import java.io.IOException;

/**
 * 预定信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@Service
public class ReservationInfoServiceImpl implements IReservationInfoService {
    @Autowired
    private ReservationInfoMapper reservationInfoMapper;

    @Autowired
    private IRoomInfoService roomInfoService;

    /**
     * 查询预定信息
     * 
     * @param reservationId 预定信息ID
     * @return 预定信息
     */
    @Override
    public ReservationInfo selectReservationInfoById(Long reservationId) {
        return reservationInfoMapper.selectReservationInfoById(reservationId);
    }

    /**
     * 查询预定信息列表
     * 
     * @param reservationInfo 预定信息
     * @return 预定信息
     */
    @Override
    public List<ReservationInfo> selectReservationInfoList(ReservationInfo reservationInfo) {
        return reservationInfoMapper.selectReservationInfoList(reservationInfo);
    }

    /**
     * 查询预定信息列表（包含客户和房间信息）
     *
     * @param reservationInfo 预定信息
     * @return 预定信息DTO集合
     */
    @Override
    public List<ReservationInfoDTO> selectReservationInfoDTOList(ReservationInfo reservationInfo) {
        return reservationInfoMapper.selectReservationInfoDTOList(reservationInfo);
    }

    /**
     * 新增预定信息
     * 
     * @param reservationInfo 预定信息
     * @return 结果
     */
    @Override
    public int insertReservationInfo(ReservationInfo reservationInfo) {
        // 自动生成预定编号
        if (reservationInfo.getReservationCode() == null || reservationInfo.getReservationCode().isEmpty()) {
            reservationInfo.setReservationCode("RES" + System.currentTimeMillis());
        }
        
        // 自动填充创建者和创建时间
        reservationInfo.setCreateBy(SecurityUtils.getUsername());
        reservationInfo.setCreateTime(new Date());
        
        // 默认预定状态为已预定(0)
        if (reservationInfo.getReservationStatus() == null || reservationInfo.getReservationStatus().isEmpty()) {
            reservationInfo.setReservationStatus("0");
        }
        
        // 默认支付状态为未支付(0)
        if (reservationInfo.getPaymentStatus() == null || reservationInfo.getPaymentStatus().isEmpty()) {
            reservationInfo.setPaymentStatus("0");
        }
        
        // 生成二维码数据
        try {
            String qrContent = "预订编号: " + reservationInfo.getReservationCode() + 
                              ", 客户ID: " + reservationInfo.getCustomerId() + 
                              ", 房间ID: " + reservationInfo.getRoomId() +
                              ", 入住日期: " + reservationInfo.getCheckInDate() +
                              ", 离店日期: " + reservationInfo.getCheckOutDate() +
                              ", 总金额: " + reservationInfo.getTotalAmount();
            String qrCodeData = QRCodeUtils.generateQRCodeImage(qrContent);
            reservationInfo.setQrCodeData(qrCodeData);
        } catch (WriterException | IOException e) {
            // 如果生成二维码失败，记录日志但不影响主流程
            e.printStackTrace();
        }
        
        // 插入预定信息
        int result = reservationInfoMapper.insertReservationInfo(reservationInfo);
        
        // 如果预定成功，修改房间状态为已预定(1)
        if (result > 0 && reservationInfo.getRoomId() != null) {
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoomId(reservationInfo.getRoomId());
            roomInfo.setRoomStatus("1"); // 已预定
            roomInfo.setUpdateBy(SecurityUtils.getUsername());
            roomInfo.setUpdateTime(new Date());
            roomInfoService.updateRoomInfo(roomInfo);
        }
        
        return result;
    }

    /**
     * 修改预定信息
     * 
     * @param reservationInfo 预定信息
     * @return 结果
     */
    @Override
    public int updateReservationInfo(ReservationInfo reservationInfo) {
        // 自动填充修改者和修改时间
        reservationInfo.setUpdateBy(SecurityUtils.getUsername());
        reservationInfo.setUpdateTime(new Date());
        return reservationInfoMapper.updateReservationInfo(reservationInfo);
    }

    /**
     * 批量删除预定信息
     * 
     * @param reservationIds 需要删除的预定信息ID
     * @return 结果
     */
    @Override
    public int deleteReservationInfoByIds(Long[] reservationIds) {
        return reservationInfoMapper.deleteReservationInfoByIds(reservationIds);
    }

    /**
     * 删除预定信息信息
     * 
     * @param reservationId 预定信息ID
     * @return 结果
     */
    @Override
    public int deleteReservationInfoById(Long reservationId) {
        return reservationInfoMapper.deleteReservationInfoById(reservationId);
    }
}