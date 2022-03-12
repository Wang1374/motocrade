package com.laogeli.chatim.service;


import com.laogeli.chatim.api.dto.MessagePushDto;
import com.laogeli.chatim.api.model.MessagePush;

/**
 * description: hongpei-work com.xunke.chat.service
 *
 * @author HongPei
 * @date 2021/6/8 10:35
 */
public interface MessagePushService {

    /**
     * 根据接受者id推送消息
     * @param messagePushDto 参数类型
     */
    void MessagePushById(MessagePushDto messagePushDto);

    MessagePush findByMsgId(MessagePush messagePush);

    int ackByMsgId(MessagePush messagePush);
}
