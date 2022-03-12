package com.laogeli.chatim.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description: chat-netty-websocket com.xunke.enums
 *
 * @author HongPei
 * @date 2021/5/31 15:38
 */
@Getter
@AllArgsConstructor
public enum MsgSignFlagEnum {

    /**
     * 消息是否签收
     */
    unSign(0, "未签收"),
    signed(1, "已签收");

    public final Integer type;

    public final String value;
}
