package com.laogeli.chatim.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.laogeli.chatim.api.dto.ChatMsgDto;
import com.laogeli.chatim.api.result.ResponseResult;
import com.laogeli.chatim.constant.ChannelAttr;
import com.laogeli.chatim.constant.Constant;
import com.laogeli.chatim.api.redis.service.RedisCacheService;
import com.laogeli.chatim.enums.MsgActionEnum;
import com.laogeli.chatim.service.ChatService;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * description: 进行处理消息的Handler 发送消息
 *
 * @author HongPei
 * @date 2021/5/29 15:28
 */
@Slf4j
@Sharable // 表示它可以被多个channel安全地共享
@Component
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    @Autowired
    private ChatService chatService;

    @Autowired
    private RedisCacheService redisCacheService;

    /**
     * 读取连接消息并对消息进行处理
     *
     * @param ctx 处理上下文
     * @param msg WebSocket组件
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 文本消息
        if (msg instanceof TextWebSocketFrame) {
            String message = ((TextWebSocketFrame) msg).text();
            ChatMsgDto chatMsgDto = null;
            //TODO　发送给哪个用户
            // 从redis 中获取需要发送给哪个用户
//            Set set = redisCacheService.rangeZset("chat-loginId", 0, -1);
//            List<String> list = new ArrayList<>(set);
//            String secondLoginId = list.get(1);

            System.out.println();
            try {
                // json消息转换为Javabean对象
                chatMsgDto = JSONUtil.toBean(message, ChatMsgDto.class, true);
                log.info("收到客户端消息：{}", chatMsgDto);
            } catch (Exception e) {
                log.error("JSON格式错误!");
                sendErrorMessage(ctx, "JSON格式错误！");
                return;
            }
            if (chatMsgDto == null) {
                sendErrorMessage(ctx, "参数为空！");
                log.warn("参数为空");
                return;
            }
            // 得到消息的动作类型
            Integer action = chatMsgDto.getAction();
            switch (action) {
                // 登陆 预防同一个ip,频繁调用登陆。悉知ctx.pipeline().remove 是根据channel进行移除拦截了。如果登陆成功一次，就没有登陆拦截。
                case 0:
                    chatService.loginError(ctx.channel());
                    break;
                // 消息推送 客户端发送消息给 其他客户端
                case 1:
                    chatService.sendPush(chatMsgDto, ctx.channel());
                    break;
                // 单聊
                case 2:
//                    chatMsgDto.setReceiverId(secondLoginId);
                    chatService.sendOne(chatMsgDto, ctx.channel());
                    break;
                // 群聊
                case 3:
                    chatService.sendGroup(chatMsgDto, ctx.channel());
                    break;
                // ACK
                case 4:
                    chatService.ackMsg(chatMsgDto, ctx.channel());
                    break;
                // 已读 后期舍弃
                case 5:
                    chatService.readMsg(chatMsgDto, ctx.channel());
                    break;
                default:
                    chatService.typeError(ctx.channel());
                    break;
            }
        }

        // 二进制消息
        if (msg instanceof BinaryWebSocketFrame) {
            System.out.println("收到二进制消息：" + ((BinaryWebSocketFrame) msg).content().readableBytes());
            sendErrorMessage(ctx, "不支持二进制文件");
        }

        // ping请求
        if (msg instanceof PingWebSocketFrame) {
            System.out.println("客户端ping成功");
            ctx.channel().write(new PongWebSocketFrame(((PingWebSocketFrame) msg).content().retain()));
            return;
        }

        // 关闭连接
        if (msg instanceof CloseWebSocketFrame) {
            System.out.println("客户端关闭，通道关闭" + ctx.channel().id().asLongText());
            WebSocketServerHandshaker handsShaker = Constant.webSocketHandshakerMap.get(ctx.channel().id().asLongText());
            if (null == handsShaker) {
                sendErrorMessage(ctx, "该用户已经离线或者不存在该连接");
            } else {
                handsShaker.close(ctx.channel(), ((CloseWebSocketFrame) msg).retain());
            }
            return;
        }
    }

    /**
     * 客户端断开连接之后触发
     *
     * @param ctx 处理和上下文
     * @throws Exception 捕获异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端连接断开！！！");
        String userId = ctx.channel().attr(ChannelAttr.USER_ID).get();
        AttributeKey<Boolean> key = AttributeKey.valueOf("conflict");
        Boolean conflict = ctx.channel().attr(key).get();
        log.info("用户id：{}", userId);
        log.info("目前内存中存在的连接数：{}", Constant.onlineUserMap.toString());
        log.info("目前内存中存在的握手信息：{}", Constant.onlineUserMap.toString());
        log.info("当前关闭的管道：{}", ctx.channel());
        Constant.webSocketHandshakerMap.remove(ctx.channel().id().asLongText());
        if (StringUtils.isNotBlank(userId))
            if (conflict == null || !conflict) Constant.onlineUserMap.remove(userId);
    }

    /**
     * 出现不可抗拒因素发送错误消息给客户端
     *
     * @param context 处理上下文
     * @param message 消息文字
     */
    public void sendErrorMessage(ChannelHandlerContext context, String message) {

        String result = JSONObject.toJSONString(new ResponseResult().error(message));
//        String result = new ResponseResult().error(message).toString();
        context.channel().writeAndFlush(new TextWebSocketFrame(result));
    }

    /**
     * 出现异常后触发
     *
     * @param ctx   处理上下文
     * @param cause 异常类
     * @throws Exception 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
            cause.printStackTrace();
        } catch (Exception e) {
            log.error("netty异常:{}", e);
        } finally {
            ctx.close();
        }
    }

}
