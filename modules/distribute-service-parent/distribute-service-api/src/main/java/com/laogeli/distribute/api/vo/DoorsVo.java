package com.laogeli.distribute.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 门点返回对象
 *
 * @author beifang
 * @date 2020-12-14
 */
@Data
public class DoorsVo implements Serializable {

    /**
     * key
     */
    private String id;

    /**
     * 所属公司id
     */
    private String belongCompaniesId;

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
