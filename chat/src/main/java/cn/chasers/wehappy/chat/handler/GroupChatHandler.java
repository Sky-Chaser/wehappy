package cn.chasers.wehappy.chat.handler;

import cn.chasers.wehappy.chat.ws.WebSocketClient;
import cn.chasers.wehappy.common.config.SnowflakeConfig;
import cn.chasers.wehappy.chat.handler.dispatcher.MessageHandler;
import cn.chasers.wehappy.chat.mq.Producer;
import cn.chasers.wehappy.common.msg.ProtoMsg;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * 群聊消息处理类
 *
 * @author lollipop
 * @date 2020/11/05 14:48:48
 */
@Component
@Slf4j
public class GroupChatHandler implements MessageHandler {

    private final SnowflakeConfig snowflakeConfig;
    private final Producer producer;

    @Autowired
    public GroupChatHandler(SnowflakeConfig snowflakeConfig, Producer producer) {
        this.snowflakeConfig = snowflakeConfig;
        this.producer = producer;
    }

    @Override
    public void execute(ProtoMsg.Message msg, WebSocketClient client) {
        log.info("GroupChatHandler [execute] {}", msg);
        ProtoMsg.ResponseMessage response = ProtoMsg.ResponseMessage.newBuilder()
                .setResult(true)
                .setExpose(false)
                .build();

        long sequence = snowflakeConfig.snowflakeId();

        ProtoMsg.Message replyMessage =
                ProtoMsg.Message.newBuilder()
                        .setId(msg.getId())
                        .setTo(msg.getChatMessage().getFrom())
                        .setMessageType(ProtoMsg.MessageType.RESPONSE_MESSAGE)
                        .setSequence(sequence)
                        .setResponseMessage(response)
                        .build();

        client.sendData(replyMessage);

        ProtoMsg.Message redirectMessage =
                ProtoMsg.Message.newBuilder()
                        .setMessageType(ProtoMsg.MessageType.GROUP_MESSAGE)
                        .setChatMessage(msg.getChatMessage())
                        .build();

        // TODO 获取到所有群成员id，然后设置redirectMessage的to，并把消息存入kafka
    }

    @Override
    public Integer getType() {
        return ProtoMsg.MessageType.GROUP_MESSAGE_VALUE;
    }
}
