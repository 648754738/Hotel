package com.hotel.app.domain;

import com.hotel.app.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预定信息表 hotel_reservation_info
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
public class ReservationInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 预定ID */
    private Long reservationId;

    /** 客户ID */
    private Long customerId;

    /** 房间ID */
    private Long roomId;

    /** 预定编号 */
    private String reservationCode;

    /** 入住日期 */
    private Date checkInDate;

    /** 离店日期 */
    private Date checkOutDate;

    /** 入住晚数 */
    private Integer nights;

    /** 总金额 */
    private BigDecimal totalAmount;

    /** 预定状态（0已预定 1已确认 2已入住 3已取消 4已完成） */
    private String reservationStatus;

    /** 特殊要求 */
    private String specialRequest;

    /** 联系电话 */
    private String contactPhone;

    /** 联系邮箱 */
    private String contactEmail;

    /** 支付状态（0未支付 1已支付 2已退款） */
    private String paymentStatus;

    /** 二维码图片Base64数据 */
    private String qrCodeData;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

}