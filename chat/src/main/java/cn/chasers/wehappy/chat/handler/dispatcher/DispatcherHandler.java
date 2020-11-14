package cn.chasers.wehappy.chat.handler.dispatcher;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 消息处理分发器
 *
 * @author lollipop
 * @date 2020/11/05 11:15:08
 */
@ChannelHandler.Sharable
@Component
public class DispatcherHandler extends SimpleChannelInboundHandler<ProtoMsg.Message> {

    private final MessageHandlerContainer messageHandlerContainer;
    private final ExecutorService executor;

    @Autowired
    public DispatcherHandler(MessageHandlerContainer messageHandlerContainer) {
        this.messageHandlerContainer = messageHandlerContainer;
        executor = new ThreadPoolExecutor(
                200,
                200,
                30,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtoMsg.Message msg) throws Exception {
        MessageHandler messageHandler = messageHandlerContainer.getMessageHandler(msg.getMessageTypeValue());
        executor.submit(() -> messageHandler.execute(ctx.channel(), msg));
    }
}
