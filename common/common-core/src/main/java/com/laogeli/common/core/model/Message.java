package com.laogeli.common.core.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装返回状态
 *
 * @author wang
 * @date 2019-12-31
 */
@Data
public class Message implements Serializable {

    /**
     * 状态码 -1失败
     */
    private int code = -1;

    /**
     * 消息 -1失败
     */
    private String msg ;

    /**
     * 状态  -2: 验证码已过期，请从新获取.   -1: 验证码错误，请重新输入   1： 绑定成功
     */
    private String states ;

    public Message() {
        super();
    }

    public Message(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }
}
