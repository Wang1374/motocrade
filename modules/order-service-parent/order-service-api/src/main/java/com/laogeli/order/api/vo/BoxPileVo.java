package com.laogeli.order.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 箱型返回对象
 *
 * @author yangyu
 * @date 2020-07-03
 */
@Data
public class BoxPileVo implements Serializable {

    /**
     * key
     */
    private int id;

    /**
     * 箱型代码
     */
    private String label;

    /**
     * 箱型名
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
