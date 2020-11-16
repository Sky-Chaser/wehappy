package cn.chasers.wehappy.chat.handler;

import cn.chasers.wehappy.chat.ws.WebSocketClient;
import cn.chasers.wehappy.common.config.SnowflakeConfig;
import cn.chasers.wehappy.chat.handler.dispatcher.MessageHandler;
import cn.chasers.wehappy.chat.mq.Producer;
import cn.chasers.wehappy.common.msg.ProtoMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 私聊消息处理类
 *
 * @author lollipop
 * @date 2020/11/05 14:48:48
 */
@Component
@Slf4j
public class SingleChatHandler implements MessageHandler {

    private final SnowflakeConfig snowflakeConfig;
    private final Producer producer;

    @Autowired
    public SingleChatHandler(SnowflakeConfig snowflakeConfig, Producer producer) {
        this.snowflakeConfig = snowflakeConfig;
        this.producer = producer;
    }

    @Override
    public void execute(ProtoMsg.Message msg, WebSocketClient client) {
        log.info("SingleChatHandler [execute] {}", msg);
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
                        .setTo(msg.getChatMessage().getTo())
                        .setMessageType(ProtoMsg.MessageType.SINGLE_MESSAGE)
                        .setChatMessage(msg.getChatMessage())
                        .build();

        producer.sendMessage(redirectMessage);
    }

    @Override
    public Integer getType() {
        return ProtoMsg.MessageType.SINGLE_MESSAGE_VALUE;
    }
}
