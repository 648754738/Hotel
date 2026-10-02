package com.hotel.app.domain;

import com.hotel.app.common.annotation.Excel;
import com.hotel.app.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 客户信息表 hotel_customer_info
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@Data
public class CustomerInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 客户ID */
    private Long customerId;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCard;

    /** 性别（0男 1女） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String gender;

    /** 出生日期 */
    @Excel(name = "出生日期")
    private Date birthday;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 紧急联系人 */
    @Excel(name = "紧急联系人")
    private String emergencyContact;

    /** 紧急联系电话 */
    @Excel(name = "紧急联系电话")
    private String emergencyPhone;

}