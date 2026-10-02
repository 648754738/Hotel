package com.hotel.app.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotel.app.common.utils.SecurityUtils;
import com.hotel.app.domain.RoomType;
import com.hotel.app.mapper.RoomTypeMapper;
import com.hotel.app.service.IRoomTypeService;

/**
 * 客房类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@Service
public class RoomTypeServiceImpl implements IRoomTypeService {
    @Autowired
    private RoomTypeMapper roomTypeMapper;

    /**
     * 查询客房类型
     * 
     * @param typeId 客房类型ID
     * @return 客房类型
     */
    @Override
    public RoomType selectRoomTypeById(Long typeId) {
        return roomTypeMapper.selectRoomTypeById(typeId);
    }

    /**
     * 查询客房类型列表
     * 
     * @param roomType 客房类型
     * @return 客房类型
     */
    @Override
    public List<RoomType> selectRoomTypeList(RoomType roomType) {
        return roomTypeMapper.selectRoomTypeList(roomType);
    }

    /**
     * 新增客房类型
     * 
     * @param roomType 客房类型
     * @return 结果
     */
    @Override
    public int insertRoomType(RoomType roomType) {
        // 设置创建人和创建时间
        String username = SecurityUtils.getUsername();
        roomType.setCreateBy(username);
        roomType.setCreateTime(new Date());
        // 同时设置更新人和更新时间
        roomType.setUpdateBy(username);
        roomType.setUpdateTime(new Date());
        return roomTypeMapper.insertRoomType(roomType);
    }

    /**
     * 修改客房类型
     * 
     * @param roomType 客房类型
     * @return 结果
     */
    @Override
    public int updateRoomType(RoomType roomType) {
        // 检查如果房间类型下有关联房间，则不能将状态修改为停用
        if ("1".equals(roomType.getStatus())) {
            int roomCount = roomTypeMapper.selectRoomCountByTypeId(roomType.getTypeId());
            if (roomCount > 0) {
                throw new RuntimeException("该客房类型下有关联房间，不能修改为停用状态");
            }
        }
        // 设置更新人和更新时间
        String username = SecurityUtils.getUsername();
        roomType.setUpdateBy(username);
        roomType.setUpdateTime(new Date());
        return roomTypeMapper.updateRoomType(roomType);
    }

    /**
     * 批量删除客房类型
     * 
     * @param typeIds 需要删除的客房类型ID
     * @return 结果
     */
    @Override
    public int deleteRoomTypeByIds(Long[] typeIds) {
        // 检查每个客房类型是否有关联房间
        for (Long typeId : typeIds) {
            int roomCount = roomTypeMapper.selectRoomCountByTypeId(typeId);
            if (roomCount > 0) {
                throw new RuntimeException("客房类型下有关联房间，不能删除");
            }
            
            // 检查客房类型状态，正常状态的房间类型不能直接删除
            RoomType roomType = roomTypeMapper.selectRoomTypeById(typeId);
            if (roomType != null && "0".equals(roomType.getStatus())) {
                throw new RuntimeException("正常状态的客房类型不能直接删除，请先停用再删除");
            }
        }
        return roomTypeMapper.deleteRoomTypeByIds(typeIds);
    }

    /**
     * 删除客房类型信息
     * 
     * @param typeId 客房类型ID
     * @return 结果
     */
    @Override
    public int deleteRoomTypeById(Long typeId) {
        // 检查客房类型是否有关联房间
        int roomCount = roomTypeMapper.selectRoomCountByTypeId(typeId);
        if (roomCount > 0) {
            throw new RuntimeException("该客房类型下有关联房间，不能删除");
        }
        
        // 检查客房类型状态，正常状态的房间类型不能直接删除
        RoomType roomType = roomTypeMapper.selectRoomTypeById(typeId);
        if (roomType != null && "0".equals(roomType.getStatus())) {
            throw new RuntimeException("正常状态的客房类型不能直接删除，请先停用再删除");
        }
        
        return roomTypeMapper.deleteRoomTypeById(typeId);
    }
}