package cn.chasers.wehappy.chat.handler;

import cn.chasers.wehappy.chat.codec.MessageDecoder;
import cn.chasers.wehappy.chat.codec.MessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * ChatServer 初始化类
 *
 * @author lollipop
 * @date 2020/11/4
 */
public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //监控线程空闲事件
        pipeline.addLast("idleStateHandler", new IdleStateHandler(60, 60, 30));
        // websocket基于http协议，所以需要http编解码器
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        // 添加对于读写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        // 对httpMessage进行聚合
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
        pipeline.addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast("messageDecoder", new MessageDecoder());
        pipeline.addLast("messageEncoder", new MessageEncoder());
//        pipeline.addLast("dispatcherHandler", new DispatcherHandler());
    }
}
