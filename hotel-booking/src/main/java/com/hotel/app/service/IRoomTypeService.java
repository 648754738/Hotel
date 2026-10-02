package com.hotel.app.service;

import com.hotel.app.domain.RoomType;
import java.util.List;

/**
 * 客房类型Service接口
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
public interface IRoomTypeService {
    /**
     * 查询客房类型
     * 
     * @param typeId 客房类型ID
     * @return 客房类型
     */
    public RoomType selectRoomTypeById(Long typeId);

    /**
     * 查询客房类型列表
     * 
     * @param roomType 客房类型
     * @return 客房类型集合
     */
    public List<RoomType> selectRoomTypeList(RoomType roomType);

    /**
     * 新增客房类型
     * 
     * @param roomType 客房类型
     * @return 结果
     */
    public int insertRoomType(RoomType roomType);

    /**
     * 修改客房类型
     * 
     * @param roomType 客房类型
     * @return 结果
     */
    public int updateRoomType(RoomType roomType);

    /**
     * 批量删除客房类型
     * 
     * @param typeIds 需要删除的客房类型ID
     * @return 结果
     */
    public int deleteRoomTypeByIds(Long[] typeIds);

    /**
     * 删除客房类型信息
     * 
     * @param typeId 客房类型ID
     * @return 结果
     */
    public int deleteRoomTypeById(Long typeId);
}