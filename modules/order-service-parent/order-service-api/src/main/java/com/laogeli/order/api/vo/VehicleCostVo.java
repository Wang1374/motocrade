package com.laogeli.order.api.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 车辆成本vo
 *
 * @author wang
 * @date 2020-10-20
 */
@Data
public class VehicleCostVo {

    /**
     * 车牌号
     */
    private String vehicle;

    /**
     * 费用
     */
    private BigDecimal totalPrice;

    /**
     * 一月费用
     */
    private BigDecimal Jan;

    /**
     * 二月费用
     */
    private BigDecimal Feb;

    /**
     * 三月费用
     */
    private BigDecimal Mar;

    /**
     * 四月费用
     */
    private BigDecimal Apr;

    /**
     * 五月费用
     */
    private BigDecimal May;

    /**
     * 六月费用
     */
    private BigDecimal June;

    /**
     * 七月费用
     */
    private BigDecimal July;

    /**
     * 八月费用
     */
    private BigDecimal Aug;

    /**
     * 九月费用
     */
    private BigDecimal Sept;

    /**
     * 十月费用
     */
    private BigDecimal Oct;

    /**
     * 十一费用
     */
    private BigDecimal Nov;

    /**
     * 十二费用
     */
    private BigDecimal Dece;
}
