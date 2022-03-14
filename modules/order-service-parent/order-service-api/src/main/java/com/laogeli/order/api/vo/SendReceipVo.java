package com.laogeli.order.api.vo;

import lombok.Data;

/**
 * @author wang
 * @Date 2021-07-02 9:09
 **/
@Data
public class SendReceipVo {

    /**
     * key
     */
    private String id;

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 详细地址
     */
    private String address;


}
