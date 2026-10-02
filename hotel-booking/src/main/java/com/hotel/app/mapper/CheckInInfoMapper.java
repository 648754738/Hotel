package com.hotel.app.mapper;

import java.util.List;
import com.hotel.app.domain.CheckInInfo;

/**
 * 入住信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
public interface CheckInInfoMapper {
    /**
     * 查询入住信息
     * 
     * @param checkInId 入住信息ID
     * @return 入住信息
     */
    public CheckInInfo selectCheckInInfoById(Long checkInId);

    /**
     * 查询入住信息列表
     * 
     * @param checkInInfo 入住信息
     * @return 入住信息集合
     */
    public List<CheckInInfo> selectCheckInInfoList(CheckInInfo checkInInfo);

    /**
     * 新增入住信息
     * 
     * @param checkInInfo 入住信息
     * @return 结果
     */
    public int insertCheckInInfo(CheckInInfo checkInInfo);

    /**
     * 修改入住信息
     * 
     * @param checkInInfo 入住信息
     * @return 结果
     */
    public int updateCheckInInfo(CheckInInfo checkInInfo);

    /**
     * 删除入住信息
     * 
     * @param checkInId 入住信息ID
     * @return 结果
     */
    public int deleteCheckInInfoById(Long checkInId);

    /**
     * 批量删除入住信息
     * 
     * @param checkInIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCheckInInfoByIds(Long[] checkInIds);
}