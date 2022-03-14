package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 门店信息
 *
 * @author wang
 * @date 2020-06-15
 */
@Data
public class Doors extends BaseEntity<Doors> {

    /**
     * 客户id
     */
    private String customerId;

    /**
     *  客户名
     */
    private String customerName;

    /**
     * 门点简称
     */
    private String doorAs;

    /**
     * 门点全称
     */
    private String doorFullName;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String contactNumber;

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

    /**
     * 备注
     */
    private String remark;
}
