package com.hotel.app.domain;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 入住信息表 hotel_check_in_info
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
public class CheckInInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 入住ID */
    private Long checkInId;

    /** 预定ID */
    private Long reservationId;

    /** 房间ID */
    private Long roomId;

    /** 客户ID */
    private Long customerId;

    /** 入住编号 */
    private String checkInCode;

    /** 实际入住时间 */
    private Date actualCheckInDate;

    /** 实际离店时间 */
    private Date actualCheckOutDate;

    /** 入住人数 */
    private Integer guestCount;

    /** 身份证正面照片URL */
    private String idCardFront;

    /** 身份证反面照片URL */
    private String idCardBack;

    /** 入住状态（0入住中 1已离店） */
    private String checkInStatus;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 备注 */
    private String remark;
}