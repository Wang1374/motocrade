package com.laogeli.common.security.mobile;

import lombok.Data;

import java.io.Serializable;

/**
 * 手机登录
 * @author wang
 * @date 2019-12-31
 */
@Data
public class MobileUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 详细描述
     */
    private String userDesc;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 语言
     */
    private String languang;
}
