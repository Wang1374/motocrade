package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 订单编号设置
 *
 * @author yangyu
 * @date 2020-06-18
 */
@Data
public class OrderNumber extends BaseEntity<OrderNumber> {

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 序列号
     */
    private int seriesNumber;

    /**
     * 序列号叠加规则 1: 分业务类型叠加 2：全部叠加
     */
    private String businessRule;

    /**
     * 序列号叠加规则 1: 按日叠加 2：按月叠加
     */
    private String dateRule;
}
