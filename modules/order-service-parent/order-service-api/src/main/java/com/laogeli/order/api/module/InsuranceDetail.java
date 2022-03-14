package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 保险详细信息
 *
 * @author wang
 * @Date 2020-07-20
 **/
@Data
@ToString(callSuper = true)
public class InsuranceDetail extends BaseEntity<InsuranceDetail> {

    /**
     * 数据id
     */
    private String id;

    /**
     * 保险详细信息id
     */
    private String insuranceDetailId;

    /**
     * 保险类别
     */
    private String insuranceCategory;

    /**
     * 保单号
     */
    private String policyNumber;

    /**
     * 保单url
     */
    private String policyUrl;

    /**
     * 保险金额
     */
    private BigDecimal insuranceAmount;

}
