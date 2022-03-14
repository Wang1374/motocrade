package com.laogeli.order.api.vo;

import lombok.Data;

/**
 * 门点返回对象
 *
 * @author wang
 * @date 2020-07-08
 */
@Data
public class DoorsVo {

    /**
     * key
     */
    private String id;

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 门点简称 value
     */
    private String value;

    /**
     * 门点简称 label
     */
    private String label;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 备注
     */
    private String remark;
}
