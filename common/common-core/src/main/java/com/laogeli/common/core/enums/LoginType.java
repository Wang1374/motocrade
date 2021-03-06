package com.laogeli.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录类型
 *
 * @author wang
 * @date 2019-12-31
 */
@Getter
@AllArgsConstructor
public enum LoginType {

    /**
     * 账号密码登录
     */
    PWD("PWD", "账号密码登录", "/oauth/token"),

    /**
     * 手机验证码登录
     */
    SMS("SMS", "验证码登录", "/mobile/token"),

    /**
     * 微信登录
     */
    WECHAT("wx", "微信登录", "/wx/token");

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口uri
     */
    private String uri;
}
