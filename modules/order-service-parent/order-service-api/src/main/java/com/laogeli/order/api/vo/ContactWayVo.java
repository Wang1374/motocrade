package com.laogeli.order.api.vo;

import lombok.Data;

/**
 * 联系方式返回对象
 *
 * @author wang
 * @date 2020-07-01
 */
@Data
public class ContactWayVo {

    /**
     * key
     */
    private String id;

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 联系人value
     */
    private String value;

    /**
     * 联系人label
     */
    private String label;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 座机号码
     */
    private String machineNumber;
}
