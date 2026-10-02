package com.hotel.app.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotel.app.domain.RoomInfo;
import com.hotel.app.domain.dto.RoomInfoDTO;
import com.hotel.app.mapper.RoomInfoMapper;
import com.hotel.app.service.IRoomInfoService;
import com.hotel.app.common.utils.SecurityUtils;

/**
 * 客房信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@Service
public class RoomInfoServiceImpl implements IRoomInfoService {
    @Autowired
    private RoomInfoMapper roomInfoMapper;

    /**
     * 查询客房信息
     * 
     * @param roomId 客房信息ID
     * @return 客房信息
     */
    @Override
    public RoomInfo selectRoomInfoById(Long roomId) {
        return roomInfoMapper.selectRoomInfoById(roomId);
    }

    /**
     * 根据房间号查询客房信息
     * 
     * @param roomNumber 房间号
     * @return 客房信息
     */
    @Override
    public RoomInfo selectRoomInfoByRoomNumber(String roomNumber) {
        return roomInfoMapper.selectRoomInfoByRoomNumber(roomNumber);
    }

    /**
     * 查询客房信息列表
     * 
     * @param roomInfo 客房信息
     * @return 客房信息
     */
    @Override
    public List<RoomInfo> selectRoomInfoList(RoomInfo roomInfo) {
        return roomInfoMapper.selectRoomInfoList(roomInfo);
    }

    /**
     * 查询客房信息列表（包含房间类型信息）
     *
     * @param roomInfo 客房信息
     * @return 客房信息DTO集合
     */
    @Override
    public List<RoomInfoDTO> selectRoomInfoDTOList(RoomInfo roomInfo) {
        return roomInfoMapper.selectRoomInfoDTOList(roomInfo);
    }

    /**
     * 新增客房信息
     * 
     * @param roomInfo 客房信息
     * @return 结果
     */
    @Override
    public int insertRoomInfo(RoomInfo roomInfo) {
        // 自动填充创建者和创建时间
        roomInfo.setCreateBy(SecurityUtils.getUsername());
        roomInfo.setCreateTime(new Date());
        // 默认房间状态为空闲状态
        if (roomInfo.getRoomStatus() == null || roomInfo.getRoomStatus().isEmpty()) {
            roomInfo.setRoomStatus("0");
        }
        return roomInfoMapper.insertRoomInfo(roomInfo);
    }

    /**
     * 修改客房信息
     * 
     * @param roomInfo 客房信息
     * @return 结果
     * @throws RuntimeException 当房间状态不为空闲时抛出异常
     */
    @Override
    public int updateRoomInfo(RoomInfo roomInfo) throws RuntimeException {
        // 检查房间状态，只有空闲状态（0）的房间才能修改
        RoomInfo existingRoom = roomInfoMapper.selectRoomInfoById(roomInfo.getRoomId());
        if (existingRoom != null && !"0".equals(existingRoom.getRoomStatus())) {
            throw new RuntimeException("只有空闲状态的房间才能修改！");
        }
        // 自动填充修改者和修改时间
        roomInfo.setUpdateBy(SecurityUtils.getUsername());
        roomInfo.setUpdateTime(new Date());
        return roomInfoMapper.updateRoomInfo(roomInfo);
    }

    /**
     * 批量删除客房信息
     * 
     * @param roomIds 需要删除的客房信息ID
     * @return 结果
     * @throws RuntimeException 当房间状态不为空闲时抛出异常
     */
    @Override
    public int deleteRoomInfoByIds(Long[] roomIds) throws RuntimeException {
        for (Long roomId : roomIds) {
            // 检查房间状态，只有空闲状态（0）的房间才能删除
            RoomInfo roomInfo = roomInfoMapper.selectRoomInfoById(roomId);
            if (roomInfo != null && !"0".equals(roomInfo.getRoomStatus())) {
                throw new RuntimeException("房间号为 " + roomInfo.getRoomNumber() + " 的房间状态不为空闲，不能删除！");
            }
        }
        return roomInfoMapper.deleteRoomInfoByIds(roomIds);
    }

    /**
     * 删除客房信息信息
     * 
     * @param roomId 客房信息ID
     * @return 结果
     * @throws RuntimeException 当房间状态不为空闲时抛出异常
     */
    @Override
    public int deleteRoomInfoById(Long roomId) throws RuntimeException {
        // 检查房间状态，只有空闲状态（0）的房间才能删除
        RoomInfo roomInfo = roomInfoMapper.selectRoomInfoById(roomId);
        if (roomInfo != null && !"0".equals(roomInfo.getRoomStatus())) {
            throw new RuntimeException("房间号为 " + roomInfo.getRoomNumber() + " 的房间状态不为空闲，不能删除！");
        }
        return roomInfoMapper.deleteRoomInfoById(roomId);
    }
}