package com.laogeli.chatim.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.laogeli.chatim.api.dto.ChatMsgDto;
import com.laogeli.chatim.api.result.ResponseResult;
import com.laogeli.chatim.constant.ChannelAttr;
import com.laogeli.chatim.constant.Constant;
import com.laogeli.chatim.enums.MsgActionEnum;
import com.laogeli.chatim.api.redis.service.RedisCacheService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Desecription: 登陆连接
 * @Author: HongPei
 * @Date: 2021/6/10 16:33
 */
@Slf4j
@Sharable
@Component
public class ConnectHandler extends ChannelInboundHandlerAdapter {


    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            String message = ((TextWebSocketFrame) msg).text();
            ChatMsgDto chatMsgDto = null;
            try {
                // json消息转换为Javabean对象
                chatMsgDto = JSONUtil.toBean(message, ChatMsgDto.class, true);
                log.info("收到客户端消息：{}", chatMsgDto);
            } catch (Exception e) {
                log.error("非JSON格式");
            }
            if (chatMsgDto != null && chatMsgDto.getAction() == 0) {
                // 双向绑定 channel -> userId
                // TODO 最好把用户登陆态存到redis中维护
                // TODO 将多端放入其中，写个工具类 拼接id 与 解析id,redis 中 保存用户多端关系，key为userId value 为拼接结果
                String loginId = chatMsgDto.getUserId() + "#" + chatMsgDto.getClientType();
                // 判断是否存在？存在的话将上一个移除，断开连接，新连接读取不到离线消息因为上一个连接已经拉取了，所以前端重新赋值就好。
                if (Constant.onlineUserMap.containsKey(loginId)) {
                    Channel channel = Constant.onlineUserMap.get(loginId);
                    String responseResult = JSONObject.toJSONString(new ResponseResult().error("新建连接成功，移除当前连接", MsgActionEnum.REMOVE.type));
                    channel.writeAndFlush(new TextWebSocketFrame(responseResult));
                    channel.attr(ChannelAttr.CONFLICT).set(true);
                    channel.close();
                    Constant.onlineUserMap.remove(loginId);
                }
                // redisCacheServiceImp.put(chatMsgDto.getUserId(), loginId);
                AttributeKey<String> userId = AttributeKey.valueOf("userId");
                ctx.channel().attr(userId).set(loginId);
                Constant.onlineUserMap.put(loginId, ctx.channel());
                String responseResult = JSONObject.toJSONString(new ResponseResult().success(MsgActionEnum.LOGIN.type)
                        .setData("loginId", loginId)
                        .setData("content", "登录授权成功!"));
                //将用户存入redis
                // TODO　
                redisCacheService.zset("chat-loginId",chatMsgDto.getUserId(),System.currentTimeMillis());
                ctx.channel().writeAndFlush(new TextWebSocketFrame(responseResult));
                // 一行代码实现逻辑的删除
                ctx.pipeline().remove(this);
            } else {
                //触发下一个handler
                ctx.fireChannelRead(msg);
            }
        }

        // 关闭连接
        if (msg instanceof CloseWebSocketFrame) {
            log.warn("关闭连接");
            WebSocketServerHandshaker handsShaker = Constant.webSocketHandshakerMap.get(ctx.channel().id().asLongText());
            handsShaker.close(ctx.channel(), ((CloseWebSocketFrame) msg).retain());
        }
    }

}
