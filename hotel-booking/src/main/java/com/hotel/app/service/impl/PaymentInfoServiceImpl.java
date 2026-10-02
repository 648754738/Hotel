package com.hotel.app.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotel.app.domain.PaymentInfo;
import com.hotel.app.mapper.PaymentInfoMapper;
import com.hotel.app.service.IPaymentInfoService;

/**
 * 支付信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@Service
public class PaymentInfoServiceImpl implements IPaymentInfoService {
    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    /**
     * 查询支付信息
     * 
     * @param paymentId 支付信息ID
     * @return 支付信息
     */
    @Override
    public PaymentInfo selectPaymentInfoById(Long paymentId) {
        return paymentInfoMapper.selectPaymentInfoById(paymentId);
    }

    /**
     * 查询支付信息列表
     * 
     * @param paymentInfo 支付信息
     * @return 支付信息
     */
    @Override
    public List<PaymentInfo> selectPaymentInfoList(PaymentInfo paymentInfo) {
        return paymentInfoMapper.selectPaymentInfoList(paymentInfo);
    }

    /**
     * 新增支付信息
     * 
     * @param paymentInfo 支付信息
     * @return 结果
     */
    @Override
    public int insertPaymentInfo(PaymentInfo paymentInfo) {
        return paymentInfoMapper.insertPaymentInfo(paymentInfo);
    }

    /**
     * 修改支付信息
     * 
     * @param paymentInfo 支付信息
     * @return 结果
     */
    @Override
    public int updatePaymentInfo(PaymentInfo paymentInfo) {
        return paymentInfoMapper.updatePaymentInfo(paymentInfo);
    }

    /**
     * 批量删除支付信息
     * 
     * @param paymentIds 需要删除的支付信息ID
     * @return 结果
     */
    @Override
    public int deletePaymentInfoByIds(Long[] paymentIds) {
        return paymentInfoMapper.deletePaymentInfoByIds(paymentIds);
    }

    /**
     * 删除支付信息信息
     * 
     * @param paymentId 支付信息ID
     * @return 结果
     */
    @Override
    public int deletePaymentInfoById(Long paymentId) {
        return paymentInfoMapper.deletePaymentInfoById(paymentId);
    }
}