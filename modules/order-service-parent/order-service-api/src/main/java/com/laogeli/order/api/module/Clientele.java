package com.laogeli.order.api.module;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 客户信息
 *
 * @author yangyu
 * @date 2020-06-09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Clientele extends BaseEntity<Clientele> {

    /**
     * 公司抬头
     */
    private String companyName;

    /**
     * 公司代码
     */
    private String companyCode;

    /**
     * 统一社会信用代码
     */
    private String uscc;

    /**
     * 付款人
     */
    private String payer;

    /**
     * 合作伙伴： 1 固定  2 非固定
     */
    private int partner;

    /**
     * 性质 ： 1 客户， 2 供应商, 3 往来单位
     */
    private int nature;

    /**
     * 类型 ： 1 公司， 2 个人
     */
    private int type;

    /**
     * 业务人员
     */
    private String salesman;

    /**
     * 对账方式: 1 单票， 2 多票
     */
    private int howToAccount;

    /**
     * 结算方式： 1 票结， 2 月结
     */
    private int clearingForm;

    /**
     * 结算周期
     */
    private int settlementInterval;

    /**
     * 账期
     */
    private int paymentDays;

    /**
     * EIR
     */
    private String eir;

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
     * 中文详细地址
     */
    private String chineseAddress;

    /**
     * 英文名称
     */
    private String englishName;

    /**
     * 英文公司地址
     */
    private String englishAddress;

    /**
     * 备注
     */
    private String remark;

    private int queryType;

}
