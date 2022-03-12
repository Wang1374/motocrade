package com.laogeli.order.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 船公司返回对象
 *
 * @author yangyu
 * @date 2020-07-02
 */
@Data
public class ShipCompanyVo implements Serializable {

    /**
     * key
     */
    private int id;

    /**
     * 船公司代码
     */
    private String label;

    /**
     * 船公司名
     */
    private String value;

    /**
     * 顺序
     */
    private int order;

    /**
     * 类型 1：外贸 2：内贸 3：内外贸
     */
    private int type;
}
