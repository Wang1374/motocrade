package com.laogeli.order.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 码头返回对象
 *
 * @author yangyu
 * @date 2020-07-03
 */
@Data
public class DockVo implements Serializable {
    /**
     * key
     */
    private String id;

    /**
     * 码头名
     */
    private String label;

    /**
     * 码头名
     */
    private String value;

    /**
     * 顺序
     */
    private int order;

    /**
     * 类型
     */
    private int type;
}
