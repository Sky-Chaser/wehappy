package cn.chasers.wehappy.chat.server;

import cn.chasers.wehappy.chat.config.NettyConfig;
import cn.chasers.wehappy.chat.handler.ChatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * Netty服务类
 *
 * @author lollipop
 * @date 2020/11/4
 */
@Slf4j
@Component
public class ChatServer {

    private final NettyConfig nettyConfig;

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    @Autowired
    public ChatServer(NettyConfig nettyConfig) {
        this.nettyConfig = nettyConfig;
        bossGroup = new NioEventLoopGroup(nettyConfig.getBossThreadNum());
        workerGroup = new NioEventLoopGroup(nettyConfig.getWorkerThreadNum());
    }

    public void start() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    // 1 设置reactor 线程
                    .group(bossGroup, workerGroup)
                    // 2 设置nio类型的channel
                    .channel(NioServerSocketChannel.class)
                    // 3 设置监听端口
                    .localAddress(new InetSocketAddress(nettyConfig.getPort()))
                    // 4 设置通道选项
//                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    // 5 装配流水线
                    .childHandler(new ChatServerInitializer());

            // 6 开始绑定 server 通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            log.info("chat server start, endpoint: " + channelFuture.channel().localAddress());
            // 7 监听通道关闭事件 应用程序会一直等待，直到channel关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8 优雅关闭EventLoopGroup，释放掉所有资源包括创建的线程
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
