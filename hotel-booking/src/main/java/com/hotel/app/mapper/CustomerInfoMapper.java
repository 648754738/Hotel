package com.hotel.app.mapper;

import com.hotel.app.domain.CustomerInfo;

import java.util.List;

/**
 * 客户信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
public interface CustomerInfoMapper {
    /**
     * 查询客户信息
     * 
     * @param customerId 客户信息ID
     * @return 客户信息
     */
    public CustomerInfo selectCustomerInfoById(Long customerId);

    /**
     * 根据身份证号查询客户信息
     * 
     * @param idCard 身份证号
     * @return 客户信息
     */
    public CustomerInfo selectCustomerInfoByIdCard(String idCard);

    /**
     * 查询客户信息列表
     * 
     * @param customerInfo 客户信息
     * @return 客户信息集合
     */
    public List<CustomerInfo> selectCustomerInfoList(CustomerInfo customerInfo);

    /**
     * 新增客户信息
     * 
     * @param customerInfo 客户信息
     * @return 结果
     */
    public int insertCustomerInfo(CustomerInfo customerInfo);

    /**
     * 修改客户信息
     * 
     * @param customerInfo 客户信息
     * @return 结果
     */
    public int updateCustomerInfo(CustomerInfo customerInfo);

    /**
     * 删除客户信息
     * 
     * @param customerId 客户信息ID
     * @return 结果
     */
    public int deleteCustomerInfoById(Long customerId);

    /**
     * 批量删除客户信息
     * 
     * @param customerIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCustomerInfoByIds(Long[] customerIds);
    
    /**
     * 查询总客户数
     * 
     * @return 总客户数
     */
    public int selectTotalCustomerCount();
}