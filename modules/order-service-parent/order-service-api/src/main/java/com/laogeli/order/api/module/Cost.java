package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 费用信息
 *
 * @author yangyu
 * @date 2020-08-27
 **/
@Data
public class Cost extends BaseEntity<Cost> {

    /**
     * 做箱id
     */
    private String mcId;

    /**
     * 费用所属车辆
     */
    private String vehicleCost;

    /**
     * 往来单位id
     */
    private String btypeId;

    /**
     * 往来单位
     */
    private String btype;

    /**
     * 合作伙伴： 1 固定  2 非固定
     */
    private int partner;

    /**
     * 付款人
     */
    private String payer;

    /**
     * 收费项目
     */
    private String payItems;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 数量
     */
    private BigDecimal amount;

    /**
     * 总金额
     */
    private BigDecimal totalPrice;

    /**
     * 确认
     */
    private Boolean affirmState;

    /**
     * 应收开票/实付开票
     */
    private Boolean invoiceState;

    /**
     * 实收/实付
     */
    private Boolean paymentReceivedState;

    /**
     *
     * 费用状态 1：确认 2：应收开票/应付开票 3：实收/实付
     */
    private int expenseStatus;

    /**
     * 发票号
     */
    private String invoiceNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 费用类型 1：应收费用 2：应付费用 3：其他应收费用 4：其他应付费用 5: 平台应收费用
     */
    private int costTypes;

    /**
     * 代垫发票url
     */
    private String invoiceUrl;

    /**
     * 车队id/也存放客户id
     */
    private String fleetId;

    /**
     * 车队名/也存放客户名
     */
    private String fleetName;
}
