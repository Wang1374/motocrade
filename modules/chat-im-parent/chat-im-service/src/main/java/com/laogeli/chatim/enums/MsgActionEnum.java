package com.laogeli.chatim.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MsgActionEnum {

    /**
     * 客户端连接握手
     */
    HANDSHAKE(0, "连接握手"),

    /**
     * 客户端登录授权
     */
    LOGIN(1, "登录授权"),

    /**
     * 客户端保持心跳
     */
    KEEPALIVE(2, "客户端保持心跳"),

    /**
     * 一对一消息
     */
    SINGLE_SENDING(3, "单聊"),

    /**
     * 群聊消息
     */
    GROUP_SENDING(4, "群聊"),

    /**
     * 系统推送
     */
    SYSTEM_PUSH(5, "系统推送"),

    /**
     * 系统推送
     */
    REMOVE(6, "移除当前连接"),

    /**
     * 消息发送成功回执
     */
    SUCCESS(7, "消息发送成功"),

    /**
     * 已读回执
     */
    READ(8, "已读");

    public final Integer type;

    public final String content;
}
