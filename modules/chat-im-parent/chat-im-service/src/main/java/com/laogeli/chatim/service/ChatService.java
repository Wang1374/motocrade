package com.laogeli.chatim.service;

import com.laogeli.chatim.api.dto.ChatMsgDto;
import io.netty.channel.Channel;

/**
 * description: 聊天服务
 *
 * @author HongPei
 * @date 2021/5/29 15:35
 */
//TODO
public interface ChatService {

    void sendPush(ChatMsgDto chatMsgDto, Channel channel);

    /**
     * 用户-用户之间相互之间发送消息
     * @param chatMsgDto 参数类型
     * @param channel 管道
     */
    void sendOne(ChatMsgDto chatMsgDto, Channel channel);

    /**
     * 用户-群之间相互发送消息
     * @param chatMsgDto 参数类型
     * @param channel 管道
     */
    void sendGroup(ChatMsgDto chatMsgDto, Channel channel);

    void ackMsg(ChatMsgDto chatMsgDto, Channel channel);

    void readMsg(ChatMsgDto chatMsgDto, Channel channel);

    void typeError(Channel channel);

    void loginError(Channel channel);
}
