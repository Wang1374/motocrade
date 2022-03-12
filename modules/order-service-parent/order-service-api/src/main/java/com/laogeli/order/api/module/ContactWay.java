package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 客户联系方式
 *
 * @author yangyu
 * @date 2020-06-11
 */
@Data
public class ContactWay extends BaseEntity<ContactWay> {

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 座机号码
     */
    private String machineNumber;

    /**
     * QQ
     */
    private String qq;

    /**
     * 邮箱
     */
    private String email;
}
