package com.hotel.app.domain;

import com.hotel.app.common.annotation.Excel;
import com.hotel.app.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 客房信息表 hotel_room_info
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
public class RoomInfo extends BaseEntity {
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

}