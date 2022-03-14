package com.laogeli.distribute.api.module;

import lombok.Data;

/**
 * 件毛体信息
 *
 * @author wang
 * @date 2020-12-16
 **/
@Data
public class HairBody {

    /**
     * 提单号
     */
    private String blNo;

    /**
     * 件数
     */
    private double total;

    /**
     * 毛重
     */
    private double grossWeight;

    /**
     * 体积
     */
    private double cbm;

    /**
     * 品名
     */
    private String productName;
}
