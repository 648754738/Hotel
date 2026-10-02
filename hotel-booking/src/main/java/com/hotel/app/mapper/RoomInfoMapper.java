package com.hotel.app.mapper;

import java.util.List;
import java.util.Map;
import com.hotel.app.domain.RoomInfo;
import com.hotel.app.domain.dto.RoomInfoDTO;

/**
 * 客房信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
public interface RoomInfoMapper {
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
     */
    public int updateRoomInfo(RoomInfo roomInfo);

    /**
     * 删除客房信息
     * 
     * @param roomId 客房信息ID
     * @return 结果
     */
    public int deleteRoomInfoById(Long roomId);

    /**
     * 批量删除客房信息
     * 
     * @param roomIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoomInfoByIds(Long[] roomIds);
    
    /**
     * 根据状态查询客房数量
     * 
     * @param status 状态
     * @return 客房数量
     */
    public int selectRoomCountByStatus(String status);
    
    /**
     * 查询总房间数
     * 
     * @return 总房间数
     */
    public int selectTotalRoomCount();
}