package com.laogeli.chatim.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.laogeli.chatim.api.dto.ChatMsgDto;
import com.laogeli.chatim.api.result.ResponseResult;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Desecription: 登录校验
 * @Author: HongPei
 * @Date: 2021/6/10 16:33
 */
@Slf4j
@Sharable
@Component
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 是登陆 就不做登陆校验
        String message = ((TextWebSocketFrame) msg).text();
        ChatMsgDto chatMsgDto = null;
        AttributeKey<String> key = AttributeKey.valueOf("userId");
        String userId = ctx.channel().attr(key).get();
        log.info("登录校验：{}", userId);
        if (StringUtils.isBlank(userId)) {
            try {
                chatMsgDto = JSONUtil.toBean(message, ChatMsgDto.class, true);
                log.info("收到客户端消息：{}", chatMsgDto);
            } catch (Exception e) {
                log.error("JSON转换对象异常");
            }
            // 如果是登陆请求，进入登陆拦截中，若不是登陆请求，返回未登陆消息。
            if (chatMsgDto != null && chatMsgDto.getAction() == 0) {
                // 触发下一个handler
                ctx.fireChannelRead(msg);
            } else {
                String result = JSONObject.toJSONString(new ResponseResult().error("未登录，请登录后再发送..."));
//                String result = new ResponseResult().error("未登录，请登录后再发送...").toString();
                ctx.channel().writeAndFlush(new TextWebSocketFrame(result));
            }
        } else {
            // 一行代码实现逻辑的删除
            ctx.pipeline().remove(this);
            // 带上连接信息，进入下一个channelRead
            super.channelRead(ctx, msg);
        }
    }
}
