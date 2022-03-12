package com.laogeli.chatim.handler;

import com.alibaba.fastjson.JSONObject;
import com.laogeli.chatim.api.result.ResponseResult;
import com.laogeli.chatim.constant.Constant;
import com.laogeli.chatim.enums.MsgActionEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;


/**
 * description: 主要是处理Http升级为WebSocket ws协议, 保持长连接
 *
 * @author HongPei
 * @date 2021/5/29 15:02
 */
@Slf4j
@Sharable // 表示它可以被多个channel安全地共享
@Component
public class NettyHttpRequestHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * 读取完连接的消息后，对消息进行处理。
     * 这里仅处理HTTP请求，WebSocket请求交给下一个处理器。
     *
     * @param context 处理上下文
     * @param msg     接收对象
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, Object msg) {
        // 如果是Http请求 需要进行协议升级
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(context, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            // 处理websocket客户端的消息
            context.fireChannelRead(((WebSocketFrame) msg).retain());
        }
    }

    /**
     * 处理Http请求，主要是完成HTTP协议到Websocket协议的升级
     *
     * @param context 处理上下文
     * @param request 消息请求
     */
    private void handleHttpRequest(ChannelHandlerContext context, FullHttpRequest request) {
        if (!request.decoderResult().isSuccess()) {
            // 若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
            sendHttpResponse(context, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        // 协议升级
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory("ws:/" + context.channel() + "/websocket", null, false);
        WebSocketServerHandshaker handsShaker = factory.newHandshaker(request);
        // 存储握手信息
        Constant.webSocketHandshakerMap.put(context.channel().id().asLongText(), handsShaker);
        if (handsShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(context.channel());
        } else {
            //获取当前channel绑定的IP地址
            InetSocketAddress ipSocket = (InetSocketAddress) context.channel().remoteAddress();
            String address = ipSocket.getAddress().getHostAddress();
            // 表示握手成功
            handsShaker.handshake(context.channel(), request);
            log.info("Http->websocket握手协议升级成功啦！address为：{}", address);
            String responseResult = JSONObject.toJSONString(new ResponseResult().success(MsgActionEnum.HANDSHAKE.type)
                    .setData("content", "websocket连接成功!"));
            context.channel().writeAndFlush(new TextWebSocketFrame(responseResult));
        }
    }

    /**
     * 消息处理失败 发送一个失败请求 应答客户端
     *
     * @param context                 处理上下文
     * @param request                 请求
     * @param defaultFullHttpResponse 默认的Http响应
     */
    private void sendHttpResponse(ChannelHandlerContext context, FullHttpRequest request, DefaultFullHttpResponse defaultFullHttpResponse) {
        log.error("消息处理失败:{}", defaultFullHttpResponse.status().code());
        if (defaultFullHttpResponse.status().code() != HttpResponseStatus.OK.code()) {
            ByteBuf buf = Unpooled.copiedBuffer(defaultFullHttpResponse.status().toString(), CharsetUtil.UTF_8);
            defaultFullHttpResponse.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        boolean keepLive = HttpUtil.isKeepAlive(request);
        ChannelFuture future = context.channel().writeAndFlush(request);
        if (!keepLive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 异常处理
     *
     * @param ctx   处理上下文
     * @param cause 抛出异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            cause.printStackTrace();
        } catch (Exception e) {
            log.error("连接异常:{}", e);
        } finally {
            ctx.close();
        }
    }
}
