package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆保养记录
 * @author BeiFang
 * @Date 2020-07-20
 **/
@Data
@ToString(callSuper = true)
public class InsuranceRecord extends BaseEntity<InsuranceRecord> {

    /**
     * 保养记录id
     */
    private String id;

    /**
     * 车辆id
     */
    private String carId;

    /**
     * 保险详细信息id
     */
    private String insuranceDetailId;

    /**
     * 起始日期
     */
    private Date startDate;

    /**
     * 截止日期
     */
    private Date endDate;

    /**
     * 保险公司
     */
    private String insuranceCompany;

    /**
     * 保险总金额
     */
    private BigDecimal insuranceTotalPrice;
}
