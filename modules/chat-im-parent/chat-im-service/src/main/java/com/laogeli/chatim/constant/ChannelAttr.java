package com.laogeli.chatim.constant;

import io.netty.util.AttributeKey;

/**
 * description: hongpei-work com.xunke.chat.constant
 *
 * @author HongPei
 * @date 2021/6/16 15:42
 */
public interface ChannelAttr {

    AttributeKey<String> USER_ID = AttributeKey.valueOf("userId");

    AttributeKey<Boolean> CONFLICT = AttributeKey.valueOf("conflict");

}
