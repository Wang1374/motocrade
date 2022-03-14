package com.laogeli.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 订单号前缀类型
 *
 * @author wang
 * @date 2020-06-18
 */
@Getter
@AllArgsConstructor
public enum OrderNumberPrefix {

    SHIPPING_EXPORT("SE", 1, "海运出口订单默认前缀"),
    SEABORNE_IMPORT("SI", 2, "海运进口订单默认前缀"),
    AIR_EXPORT("AE", 3, "空运出口订单默认前缀"),
    AIR_IMPORT("AI", 4, "空运进口订单默认前缀");

    /**
     * 订单号前缀
     */
    private String prefix;

    /**
     * 订单类型
     */
    private int type;

    /**
     * 描述
     */
    private String description;

    /**
     * 根据订单类型，返回结果类型。
     *
     * @param type 订单类型
     */
    public static String getPrefix(int type) {
        for (OrderNumberPrefix value : OrderNumberPrefix.values()) {
            if (value.getType() == type) {
                return value.getPrefix();
            }
        }
        return "";
    }

}
