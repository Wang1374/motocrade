package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆轮胎费用
 * @author wang
 * @Date 2021-09-09 13:37
 **/
@Data
public class CarTyre extends BaseEntity<CarTyre> {

    /**
     * 员工id
     */
    private String employeeId;

    /**
     * 供应商名称
     */
    private String employeeName;

    /**
     * 车辆
     */
    private String vehicle;

    /**
     * 日期
     */
    private Date tyreDate;

    /**
     * 付款项目
     */
    private String tyreProject;

    /**
     * 费用
     */
    private BigDecimal tyrePrice;

    /**
     * 发票号
     */
    private String invoiceNum;

    /**
     * 公里数
     */
    private String mileage;

    /**
     * 付款状态
     */
    private Boolean payStatus;

    /**
     * 胎号
     */
    private String tyreNum;

    /**
     * 附件url
     */
    private String ticketUrl;

    /**
     * 附件name
     */
    private String ticketName;


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
     * 备注
     */
    private String remark;
}
