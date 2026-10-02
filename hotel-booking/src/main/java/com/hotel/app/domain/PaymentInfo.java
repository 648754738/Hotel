package com.hotel.app.domain;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付信息表 hotel_payment_info
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
public class PaymentInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 支付ID */
    private Long paymentId;

    /** 预定ID */
    private Long reservationId;

    /** 支付金额 */
    private BigDecimal paymentAmount;

    /** 支付方式（0支付宝 1微信 2银行卡 3现金） */
    private String paymentMethod;

    /** 支付状态（0待支付 1已支付 2已退款） */
    private String paymentStatus;

    /** 交易流水号 */
    private String transactionId;

    /** 支付时间 */
    private Date paymentTime;

    /** 退款时间 */
    private Date refundTime;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 备注 */
    private String remark;
}