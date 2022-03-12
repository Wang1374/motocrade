package com.laogeli.chatim.socket;

import com.laogeli.chatim.handler.NettyWebsocketChildHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * description: 使用Netty作为通信基础的Websocket
 *
 * @author HongPei
 * @date 2021/5/29 15:16
 */
@Slf4j
@Configuration
public class NettyWebSocketServer implements Runnable {

    /*
     * 接收分配器大小
     * */
    private static final int RECVBYTE_ALLOCATOR_SIZE = 592048;

    /*
     * 端口
     * */
    @Value("${netty.port}")
    private Integer port;

    private ServerBootstrap serverBootstrap = new ServerBootstrap();

    // accept 新连接的线程组
    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    // 处理每一条连接的数据读写的线程组
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Autowired
    private NettyWebsocketChildHandler nettyWebsocketChildHandler;

    @Override
    public void run() {
        build();
    }

    /**
     * 启动NettyWebSocket
     */
    private void build() {
        try {
            Long beginTime = System.currentTimeMillis();
            serverBootstrap.group(bossGroup, workerGroup) // 绑定线程池
                    // 指定是Nio通信服务
                    .channel(NioServerSocketChannel.class)
                    // 绑定监听端口
                    .localAddress(port)
                    // TCP参数配置 握手字符串长度设置
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 设置TCP NO_DELAY 算法 尽量发送大文件包
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 开启心跳模式
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 配置固定长度接收缓存内存分配
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(RECVBYTE_ALLOCATOR_SIZE))
                    .childHandler(nettyWebsocketChildHandler);
            // 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture future = serverBootstrap.bind().sync();
            Long endTime = System.currentTimeMillis();
            log.info("服务器启动完成，耗时:[{}]毫秒,已经在端口：[{}]进行阻塞等待", endTime - beginTime, port);
            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            // // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
