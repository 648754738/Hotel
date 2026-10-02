package com.hotel.app.mapper;

import java.util.List;
import com.hotel.app.domain.PaymentInfo;

/**
 * 支付信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
public interface PaymentInfoMapper {
    /**
     * 查询支付信息
     * 
     * @param paymentId 支付信息ID
     * @return 支付信息
     */
    public PaymentInfo selectPaymentInfoById(Long paymentId);

    /**
     * 查询支付信息列表
     * 
     * @param paymentInfo 支付信息
     * @return 支付信息集合
     */
    public List<PaymentInfo> selectPaymentInfoList(PaymentInfo paymentInfo);

    /**
     * 新增支付信息
     * 
     * @param paymentInfo 支付信息
     * @return 结果
     */
    public int insertPaymentInfo(PaymentInfo paymentInfo);

    /**
     * 修改支付信息
     * 
     * @param paymentInfo 支付信息
     * @return 结果
     */
    public int updatePaymentInfo(PaymentInfo paymentInfo);

    /**
     * 删除支付信息
     * 
     * @param paymentId 支付信息ID
     * @return 结果
     */
    public int deletePaymentInfoById(Long paymentId);

    /**
     * 批量删除支付信息
     * 
     * @param paymentIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePaymentInfoByIds(Long[] paymentIds);
}