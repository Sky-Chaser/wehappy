package cn.chasers.wehappy.chat.handler;

import cn.chasers.wehappy.common.config.SnowflakeConfig;
import cn.chasers.wehappy.chat.handler.dispatcher.MessageHandler;
import cn.chasers.wehappy.common.msg.ProtoMsg;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lollipop
 * @date 2020/11/05 14:26:21
 */
@Slf4j
@Component
public class HeartbeatRequestHandler implements MessageHandler {

    private final SnowflakeConfig snowflakeConfig;

    @Autowired
    public HeartbeatRequestHandler(SnowflakeConfig snowflakeConfig) {
        this.snowflakeConfig = snowflakeConfig;
    }

    @Override
    public void execute(Channel channel, ProtoMsg.Message msg) {
        log.info("HeartbeatRequestHandler [execute] {}", msg);
        ProtoMsg.Message message =
                ProtoMsg.Message.newBuilder()
                .setMessageType(ProtoMsg.MessageType.HEART_BRAT_MESSAGE)
                .setSequence(snowflakeConfig.snowflakeId())
                .build();
        channel.writeAndFlush(message);
    }

    @Override
    public Integer getType() {
        return ProtoMsg.MessageType.HEART_BRAT_MESSAGE_VALUE;
    }
}
