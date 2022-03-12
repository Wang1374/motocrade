package com.laogeli.chatim.service;

import io.netty.channel.Channel;

/**
 * description: netty 推送消息
 *
 * @author HongPei
 * @date 2021/6/8 10:52
 */
public interface SendMessageService {

    void sendMessageToClient(Channel channel, String msg);

    void sendMessageToClient(String userId, String msg);
}
