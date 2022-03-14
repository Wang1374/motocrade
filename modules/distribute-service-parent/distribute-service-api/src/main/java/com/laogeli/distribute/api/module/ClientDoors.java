package com.laogeli.distribute.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 客户端门点对象
 * @author wang
 * @date 2020-12-11
 */
@Data
public class ClientDoors  extends BaseEntity<ClientDoors> {

    /**
     * 门点简称
     */
    private String doorAs;

    /**
     * 修改前门点简称
     */
    private String originalDoorAs;

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

    /**
     * 消息类型
     */
    private String msgType;
}
