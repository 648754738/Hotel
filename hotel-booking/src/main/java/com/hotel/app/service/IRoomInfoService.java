package com.hotel.app.service;

import com.hotel.app.domain.RoomInfo;
import com.hotel.app.domain.dto.RoomInfoDTO;
import java.util.List;

/**
 * 客房信息Service接口
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
public interface IRoomInfoService {
    /**
     * 查询客房信息
     * 
     * @param roomId 客房信息ID
     * @return 客房信息
     */
    public RoomInfo selectRoomInfoById(Long roomId);

    /**
     * 根据房间号查询客房信息
     * 
     * @param roomNumber 房间号
     * @return 客房信息
     */
    public RoomInfo selectRoomInfoByRoomNumber(String roomNumber);

    /**
     * 查询客房信息列表
     * 
     * @param roomInfo 客房信息
     * @return 客房信息集合
     */
    public List<RoomInfo> selectRoomInfoList(RoomInfo roomInfo);

    /**
     * 查询客房信息列表（包含房间类型信息）
     *
     * @param roomInfo 客房信息
     * @return 客房信息DTO集合
     */
    public List<RoomInfoDTO> selectRoomInfoDTOList(RoomInfo roomInfo);

    /**
     * 新增客房信息
     * 
     * @param roomInfo 客房信息
     * @return 结果
     */
    public int insertRoomInfo(RoomInfo roomInfo);

    /**
     * 修改客房信息
     * 
     * @param roomInfo 客房信息
     * @return 结果
     * @throws RuntimeException 当房间状态不为空闲时抛出异常
     */
    public int updateRoomInfo(RoomInfo roomInfo) throws RuntimeException;

    /**
     * 批量删除客房信息
     * 
     * @param roomIds 需要删除的客房信息ID
     * @return 结果
     * @throws RuntimeException 当房间状态不为空闲时抛出异常
     */
    public int deleteRoomInfoByIds(Long[] roomIds) throws RuntimeException;

    /**
     * 删除客房信息信息
     * 
     * @param roomId 客房信息ID
     * @return 结果
     * @throws RuntimeException 当房间状态不为空闲时抛出异常
     */
    public int deleteRoomInfoById(Long roomId) throws RuntimeException;
}