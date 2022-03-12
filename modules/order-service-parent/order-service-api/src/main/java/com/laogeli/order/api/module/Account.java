package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 客户账户信息
 *
 * @author yangyu
 * @date 2020-06-11
 */
@Data
public class Account extends BaseEntity<Account> {

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 发票抬头
     */
    private String invoiceTitle;

    /**
     * 发票类型
     */
    private String invoiceType;

    /**
     * 社会统一信用代码
     */
    private String uscc;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 地址
     */
    private String accountAddress;

    /**
     * 电话
     */
    private String accountPhone;

    /**
     * 人民币开户行
     */
    private String rmbBank;

    /**
     * 人民币账号
     */
    private String rmbAccount;

    /**
     * 美金开户行
     */
    private String usdBank;

    /**
     * 美金账号
     */
    private String usdAccount;
}
