package com.laogeli.chatim.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * description: 初始化ChannelChildHandler连接
 *
 * @author HongPei
 * @date 2021/5/29 15:25
 */
@Component
public class NettyWebsocketChildHandler extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyHttpRequestHandler nettyHttpRequestHandler;

    @Autowired
    private ConnectHandler connectHandler;

    @Autowired
    private AuthHandler authHandler;

    @Autowired
    private HeartBeatHandler heartBeatHandler;

    @Autowired
    private NettyWebSocketServerHandler nettyWebSocketServerHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("logging",new LoggingHandler(LogLevel.INFO)); //Netty自带的日志记录handler，这个handler使用Netty的日志框架打印日志，可以打印Netty的运行日志
        // webSocket协议本身是基于http协议的，所以这边也要使用http编解码器
        ch.pipeline().addLast("http-codec", new HttpServerCodec());
        /*http数据在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合
        这就是为什么，当浏览器发送大量数据时，就会发送多次http请求
        HTTP头和body拼接成完整请求体，聚合成FullHttpRequest或FullHttpResponse*/
        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(1024 * 64));
        // 对大数据流的支持，大文件传输策略，以块的方式来写的处理器
        ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
        // Http升级为WebSocket协议
        ch.pipeline().addLast("http-handler", nettyHttpRequestHandler);
        // 第一个参数是指定读操作空闲秒数，第二个参数是指定写操作的空闲秒数，第三个参数是指定读写空闲秒数
        ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
        // 如果在多少秒内没有收到来自客户端的任何数据包（包括但不限于心跳包），将会主动断开与该客户端的连接。
        ch.pipeline().addLast("heartBeatHandler", heartBeatHandler);
        // 用户认证handler
        ch.pipeline().addLast("auth-handler", authHandler);
        // 登录连接
        ch.pipeline().addLast("connect-handler", connectHandler);
        // 进行处理消息
        ch.pipeline().addLast("websocket-handler", nettyWebSocketServerHandler);
    }
}
