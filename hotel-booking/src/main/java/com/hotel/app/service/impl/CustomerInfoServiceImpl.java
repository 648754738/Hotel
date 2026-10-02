package com.hotel.app.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotel.app.domain.CustomerInfo;
import com.hotel.app.mapper.CustomerInfoMapper;
import com.hotel.app.service.ICustomerInfoService;
import com.hotel.app.common.utils.SecurityUtils;

/**
 * 客户信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@Service
public class CustomerInfoServiceImpl implements ICustomerInfoService {
    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    /**
     * 查询客户信息
     * 
     * @param customerId 客户信息ID
     * @return 客户信息
     */
    @Override
    public CustomerInfo selectCustomerInfoById(Long customerId) {
        return customerInfoMapper.selectCustomerInfoById(customerId);
    }

    /**
     * 根据身份证号查询客户信息
     * 
     * @param idCard 身份证号
     * @return 客户信息
     */
    @Override
    public CustomerInfo selectCustomerInfoByIdCard(String idCard) {
        return customerInfoMapper.selectCustomerInfoByIdCard(idCard);
    }

    /**
     * 查询客户信息列表
     * 
     * @param customerInfo 客户信息
     * @return 客户信息
     */
    @Override
    public List<CustomerInfo> selectCustomerInfoList(CustomerInfo customerInfo) {
        return customerInfoMapper.selectCustomerInfoList(customerInfo);
    }

    /**
     * 新增客户信息
     * 
     * @param customerInfo 客户信息
     * @return 结果
     */
    @Override
    public int insertCustomerInfo(CustomerInfo customerInfo) {
        // 自动填充创建者和创建时间
        customerInfo.setCreateBy(SecurityUtils.getUsername());
        customerInfo.setCreateTime(new Date());
        return customerInfoMapper.insertCustomerInfo(customerInfo);
    }

    /**
     * 修改客户信息
     * 
     * @param customerInfo 客户信息
     * @return 结果
     */
    @Override
    public int updateCustomerInfo(CustomerInfo customerInfo) {
        // 自动填充修改者和修改时间
        customerInfo.setUpdateBy(SecurityUtils.getUsername());
        customerInfo.setUpdateTime(new Date());
        return customerInfoMapper.updateCustomerInfo(customerInfo);
    }

    /**
     * 批量删除客户信息
     * 
     * @param customerIds 需要删除的客户信息ID
     * @return 结果
     */
    @Override
    public int deleteCustomerInfoByIds(Long[] customerIds) {
        return customerInfoMapper.deleteCustomerInfoByIds(customerIds);
    }

    /**
     * 删除客户信息信息
     * 
     * @param customerId 客户信息ID
     * @return 结果
     */
    @Override
    public int deleteCustomerInfoById(Long customerId) {
        return customerInfoMapper.deleteCustomerInfoById(customerId);
    }
}