package com.hotel.app.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotel.app.domain.CheckInInfo;
import com.hotel.app.mapper.CheckInInfoMapper;
import com.hotel.app.service.ICheckInInfoService;

/**
 * 入住信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@Service
public class CheckInInfoServiceImpl implements ICheckInInfoService {
    @Autowired
    private CheckInInfoMapper checkInInfoMapper;

    /**
     * 查询入住信息
     * 
     * @param checkInId 入住信息ID
     * @return 入住信息
     */
    @Override
    public CheckInInfo selectCheckInInfoById(Long checkInId) {
        return checkInInfoMapper.selectCheckInInfoById(checkInId);
    }

    /**
     * 查询入住信息列表
     * 
     * @param checkInInfo 入住信息
     * @return 入住信息
     */
    @Override
    public List<CheckInInfo> selectCheckInInfoList(CheckInInfo checkInInfo) {
        return checkInInfoMapper.selectCheckInInfoList(checkInInfo);
    }

    /**
     * 新增入住信息
     * 
     * @param checkInInfo 入住信息
     * @return 结果
     */
    @Override
    public int insertCheckInInfo(CheckInInfo checkInInfo) {
        return checkInInfoMapper.insertCheckInInfo(checkInInfo);
    }

    /**
     * 修改入住信息
     * 
     * @param checkInInfo 入住信息
     * @return 结果
     */
    @Override
    public int updateCheckInInfo(CheckInInfo checkInInfo) {
        return checkInInfoMapper.updateCheckInInfo(checkInInfo);
    }

    /**
     * 批量删除入住信息
     * 
     * @param checkInIds 需要删除的入住信息ID
     * @return 结果
     */
    @Override
    public int deleteCheckInInfoByIds(Long[] checkInIds) {
        return checkInInfoMapper.deleteCheckInInfoByIds(checkInIds);
    }

    /**
     * 删除入住信息信息
     * 
     * @param checkInId 入住信息ID
     * @return 结果
     */
    @Override
    public int deleteCheckInInfoById(Long checkInId) {
        return checkInInfoMapper.deleteCheckInInfoById(checkInId);
    }
}