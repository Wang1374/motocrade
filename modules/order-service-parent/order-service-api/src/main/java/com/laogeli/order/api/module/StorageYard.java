package com.laogeli.order.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 堆场信息
 *
 * @author yangyu
 * @date 2020-06-15
 */
@Data
public class StorageYard extends BaseEntity<StorageYard> {

    /**
     * 堆场名称
     */
    private String name;

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
