package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 保险记录信息
 *
 * @author wang
 * @Date 2020-10-15
 **/
@Data
@ToString(callSuper = true)
public class CarGuarantee extends BaseEntity<CarGuarantee> {

    /**
     * 保险记录id
     */
    private String id;

    /**
     * 所属公司id
     */
    private String belongCompaniesId;
    /**
     * 车牌号
     */
    private String vehicle;

    /**
     * 保单号
     */
    private String policyNum;

    /**
     * 保险种类
     */
    private String guaranteeType;

    /**
     * 保险金额
     */
    private BigDecimal guaranteePrice;

    /**
     * 保险开始日期
     */
    private Date startPolicyDate;

    /**
     * 保险截止日期
     */
    private Date endPolicyDate;

    /**
     * 保单url
     */
    private String policyUrl;

    /**
     * 保单名称
     */
    private String policyName;

    /**
     * 总金额
     */
    private BigDecimal guaranteePriceSum;
}
