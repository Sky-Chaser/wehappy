package cn.chasers.wehappy.chat.handler;

import cn.chasers.wehappy.chat.ws.WebSocketClient;
import cn.chasers.wehappy.common.config.SnowflakeConfig;
import cn.chasers.wehappy.chat.handler.dispatcher.MessageHandler;
import cn.chasers.wehappy.chat.mq.Producer;
import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.common.util.MessageUtil;
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

        String sequence = String.valueOf(snowflakeConfig.snowflakeId());

        ProtoMsg.Message response = MessageUtil.newMessage(
                msg.getId(),
                sequence,
                String.valueOf(System.currentTimeMillis()),
                msg.getTo(),
                ProtoMsg.MessageType.RESPONSE_MESSAGE,
                MessageUtil.newResponseMessage(true, 0, null, false)
        );

        client.sendData(response);

        ProtoMsg.Message redirectMessage = MessageUtil.newMessage(
                msg.getId(),
                sequence,
                String.valueOf(System.currentTimeMillis()),
                msg.getTo(),
                ProtoMsg.MessageType.SINGLE_MESSAGE,
                MessageUtil.newChatMessage(
                        msg.getTo(),
                        client.getUserId().toString(),
                        msg.getChatMessage().getContentType(),
                        msg.getChatMessage().getContent()
                )
        );

        producer.sendMessage(redirectMessage);
    }

    @Override
    public Integer getType() {
        return ProtoMsg.MessageType.SINGLE_MESSAGE_VALUE;
    }
}
