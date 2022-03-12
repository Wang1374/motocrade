package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 油卡记录信息
 * @author
 * @Date 2020-07-19
 **/
@Data
@ToString(callSuper = true)
public class OilCard extends BaseEntity<OilCard> {

    /* *
     *油卡记录id
     */

    private String id;

    /**
     * 所属公司id
     */
    private String belongCompaniesId;
    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 司机手机号
     */
    private String driverPhone;

    /**
     * 车牌号
     */
    private String vehicle;

    /**
     * 加油地点
     */
    private String oilAddress;

    /**
     * 用油类型
     */
    private String oilType;
    /**
     * 加油量
     */
    private String oilCapacity;

    /**
     * 每L油价
     */
    private BigDecimal oilPrice;

    /**
     * 订单金额
     */
    private BigDecimal oilTotalPrice;

    /**
     * 服务时间
     */
    private Date oilDate;


    private BigDecimal oilCostSum;

    /**
     * 付款状态，true 付款，false 未付
     */
    private Boolean payStatus;

    /**
     * 付款说明
     */
    private String payStatement;


    /**
     * 发票号
     */
    private String invoiceNum;

    /**
     * 备注
     */
    private String remark;
}
