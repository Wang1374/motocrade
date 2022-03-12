package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆路费费用实体
 *
 * @author BeiFang
 * @date 2020-10-27
 */
@Data
public class CarRoad extends BaseEntity<CarRoad> {

    /**
     * 过路费用记录id
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
     * 发票号
     */
    private String etcInvoice;

    /**
     * 交易金额
     */
    private BigDecimal roadPrice;

    /**
     * 高速入口
     */
    private String roadExit;

    /**
     * 高速出口
     */
    private String roadEntrance;

    /**
     * 通行开始日期
     */
    private Date roadStartDate;

    /**
     * 通行截止日期
     */
    private Date roadEndDate;
}
