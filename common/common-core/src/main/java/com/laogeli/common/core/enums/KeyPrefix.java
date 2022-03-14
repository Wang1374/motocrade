package com.laogeli.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * reids key 前缀类型
 *
 * @author wang
 * @date 2020-06-19
 */
@Getter
@AllArgsConstructor
public enum KeyPrefix {

    SE_AOTU_PREFIX("order:se:", 1, "海运出口key自增数的前缀"),
    SI_AOTU_PREFIX("order:si:", 2, "海运进口key自增数的前缀"),
    AE_AOTU_PREFIX("order:ae:", 3, "空运出口key自增数的前缀"),
    AI_AOTU_PREFIX("order:ai:", 4, "空运进口key自增数的前缀");

    /**
     * key前缀
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
        for (KeyPrefix value : KeyPrefix.values()) {
            if (value.getType() == type) {
                return value.getPrefix();
            }
        }
        return "";
    }
}
