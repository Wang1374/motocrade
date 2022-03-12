package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString(callSuper = true)
public class ExtraCost extends BaseEntity<ExtraCost> {

    /**
     * 额外费用id
     */
    private String id;

    /**
     * 车牌号
     */
    private String vehicle;

    /**
     * 日期
     */
    private Date costDate;

    /**
     * 费用名目
     */
    private String costProject;

    /**
     * 金额
     */
    private BigDecimal extraCost;

    /**
     * 发票号
     */
    private String invoiceNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 额外费用和
     */
    private String extraCostSum;

    /**
     * 付款状态，true 付款，false 未付
     */
    private Boolean payStatus;

    /**
     * 付款说明
     */
    private String payStatement;

}
