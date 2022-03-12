package com.laogeli.chatim.handler;

import com.alibaba.fastjson.JSONObject;
import com.laogeli.chatim.api.result.ResponseResult;
import com.laogeli.chatim.common.IMConstant;
import com.laogeli.chatim.constant.Constant;
import com.laogeli.chatim.enums.MsgActionEnum;
import com.laogeli.chatim.api.redis.service.RedisCacheService;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * description: 实现心跳handler 超时断开客户端避免浪费资源
 *
 * @author HongPei
 * @date 2021/6/8 16:05
 */
@Slf4j
@Sharable
@Component
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("目前内存中存在的用户：{}", Constant.onlineUserMap);
        String channelId = String.valueOf(ctx.channel().id());
        // 文本消息
        if (msg instanceof TextWebSocketFrame) {
            String message = ((TextWebSocketFrame) msg).text();
            if (message.equals("PING")) {
                String responseResult = JSONObject.toJSONString(new ResponseResult().success(MsgActionEnum.KEEPALIVE.type)
                        .setData("clientIp", ctx.channel().remoteAddress())
                        .setData("content", "PONG"));
                log.info(ctx.channel().id().asLongText() + "=====" + "收到来自channel为[" + Constant.webSocketHandshakerMap.get(ctx.channel().id().asLongText()) + "]的心跳包");
                ctx.channel().writeAndFlush(new TextWebSocketFrame(responseResult));
                // 重置,直接删除
                if (redisCacheService.hasKey(IMConstant.CHAT_HEART_BEAT_PREFIX + channelId)) {
                    redisCacheService.remove(IMConstant.CHAT_HEART_BEAT_PREFIX + channelId);
                }
            } else {
                // 触发下一个handler
                ctx.fireChannelRead(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            String channelId = String.valueOf(ctx.channel().id());
            IdleStateEvent e = (IdleStateEvent) evt;
            String eventType = null;
            switch (e.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    redisCacheService.incr(IMConstant.CHAT_HEART_BEAT_PREFIX + channelId); // 读空闲的计数加1;
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
                default:
                    break;
            }
            log.info(ctx.channel().remoteAddress() + "超时事件：{}", eventType);
            // 重试3次后，关闭管道。
            String num = redisCacheService.getString(IMConstant.CHAT_HEART_BEAT_PREFIX + channelId);
            if (num != null && Integer.parseInt(num) >= 3) {
                log.info("读空闲超时重试3次，关闭连接...");
                ctx.channel().writeAndFlush(new TextWebSocketFrame("长时间未响应，为节省资源，关闭现有连接。"));
                ctx.channel().close();
                redisCacheService.remove(IMConstant.CHAT_HEART_BEAT_PREFIX + channelId);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
