package com.laogeli.chatim.service.impl;

import com.laogeli.chatim.constant.Constant;
import com.laogeli.chatim.service.SendMessageService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * description: hongpei-work com.xunke.chat.service.impl
 *
 * @author HongPei
 * @date 2021/6/8 10:53
 */
@Slf4j
@Service
public class SendMessageServiceImpl implements SendMessageService {

    @Override
    public void sendMessageToClient(Channel channel, String msg) {
        try {
            // 此处一定要注意 不是直接使用context.writeAndFlush()方法
            channel.writeAndFlush(new TextWebSocketFrame(msg));
            //AttributeMap.attr(1);
        } catch (IllegalStateException e) {
            log.error("Channel 相同，抛出异常", e);
        }
    }

    @Override
    public void sendMessageToClient(String userId, String msg) {
        try {
            for (String client : Constant.clients) {
                Channel toUserChannel = Constant.onlineUserMap.get(userId + "#" + client);
                if (toUserChannel != null) {
                    toUserChannel.writeAndFlush(new TextWebSocketFrame(msg));
                }
            }
        } catch (IllegalStateException e) {
            log.error("Channel 相同，抛出异常", e);
        }
    }
}
