package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 寄单地址
 * @author wang
 * @Date 2021-07-01 14:03
 **/
@Data
public class SendReceipAddress extends BaseEntity<SendReceipAddress> {

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系方式
     */
    private String contactWay;


    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 地址
     */
    private String address;
}
