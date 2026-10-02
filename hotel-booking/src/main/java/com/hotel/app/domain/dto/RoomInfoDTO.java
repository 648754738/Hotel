package com.hotel.app.domain.dto;

import com.hotel.app.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.hotel.app.common.annotation.Excel;

import java.math.BigDecimal;

/**
 * 客房信息DTO（包含房间类型信息）
 * 
 * @author ruoyi
 * @date 2025-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoomInfoDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 房间ID */
    private Long roomId;

    /** 房间号 */
    @Excel(name = "房间号")
    private String roomNumber;

    /** 房间类型ID */
    @Excel(name = "房间类型ID")
    private Long typeId;

    /** 楼层 */
    @Excel(name = "楼层")
    private Integer floor;

    /** 房间状态（0空闲 1已预定 2已入住 3维修中） */
    @Excel(name = "房间状态", readConverterExp = "0=空闲,1=已预定,2=已入住,3=维修中")
    private String roomStatus;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    // 房间类型相关信息
    /** 类型名称 */
    @Excel(name = "房间类型名称")
    private String typeName;

    /** 类型描述 */
    @Excel(name = "房间类型描述")
    private String typeDesc;

    /** 床位数量 */
    @Excel(name = "床位数量")
    private Integer bedCount;

    /** 最大入住人数 */
    @Excel(name = "最大入住人数")
    private Integer maxOccupancy;

    /** 房间面积(平方米) */
    @Excel(name = "房间面积(平方米)")
    private BigDecimal area;

    /** 房间价格(元/晚) */
    @Excel(name = "房间价格(元/晚)")
    private BigDecimal price;

    /** 房间设施 */
    @Excel(name = "房间设施")
    private String facility;

    /** 房间图片URL */
    @Excel(name = "房间图片URL")
    private String imageUrls;
}