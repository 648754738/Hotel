package com.hotel.app.domain.dto;

import com.hotel.app.common.annotation.Excel;
import com.hotel.app.domain.ReservationInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预定信息DTO
 * 
 * @author ruoyi
 * @date 2025-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReservationInfoDTO extends ReservationInfo {
    private static final long serialVersionUID = 1L;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 客户性别 */
    @Excel(name = "客户性别")
    private String customerGender;

    /** 房间号 */
    @Excel(name = "房间号")
    private String roomNumber;

    /** 房间类型名称 */
    @Excel(name = "房间类型")
    private String roomTypeName;

    /** 房间最大入住人数 */
    @Excel(name = "最大入住人数")
    private Integer roomMaxOccupancy;

    /** 房间设施 */
    @Excel(name = "房间设施")
    private String facility;

    /** 房间图片URL */
    @Excel(name = "房间图片")
    private String imageUrls;

    /** 二维码图片Base64数据 */
    @Excel(name = "二维码数据")
    private String qrCodeData;
}