package com.hotel.app.mapper;

import java.util.List;
import java.util.Map;
import com.hotel.app.domain.RoomType;

/**
 * 客房类型Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
public interface RoomTypeMapper {
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
     * 删除客房类型
     * 
     * @param typeId 客房类型ID
     * @return 结果
     */
    public int deleteRoomTypeById(Long typeId);

    /**
     * 批量删除客房类型
     * 
     * @param typeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoomTypeByIds(Long[] typeIds);
    
    /**
     * 查询客房类型关联的房间数量
     * 
     * @param typeId 客房类型ID
     * @return 房间数量
     */
    public int selectRoomCountByTypeId(Long typeId);
    
    /**
     * 查询客房类型数量统计
     * 
     * @return 客房类型数量统计
     */
    public List<Map<String, Object>> selectRoomTypeCount();
    
    /**
     * 查询所有客房类型
     * 
     * @return 客房类型列表
     */
    public List<RoomType> selectAllRoomType();
}