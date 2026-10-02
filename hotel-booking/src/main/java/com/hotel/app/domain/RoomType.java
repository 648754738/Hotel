package com.hotel.app.domain;

import com.hotel.app.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客房类型表 hotel_room_type
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
public class RoomType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 类型ID */
    private Long typeId;

    /** 类型名称 */
    private String typeName;

    /** 类型描述 */
    private String typeDesc;

    /** 床位数量 */
    private Integer bedCount;

    /** 最大入住人数 */
    private Integer maxOccupancy;

    /** 房间面积(平方米) */
    private BigDecimal area;

    /** 房间价格(元/晚) */
    private BigDecimal price;

    /** 房间设施 */
    private String facility;

    /** 房间图片URL */
    private String imageUrl;

    /** 房间图片URL */
    private String imageUrls;

    /** 状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}