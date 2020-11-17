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

        String sequence = String.valueOf(snowflakeConfig.snowflakeId());

        ProtoMsg.Message response = MessageUtil.newMessage(
                msg.getId(),
                sequence,
                String.valueOf(System.currentTimeMillis()),
                msg.getChatMessage().getFrom(),
                ProtoMsg.MessageType.RESPONSE_MESSAGE,
                MessageUtil.newResponseMessage(true, 0, null, false)
        );

        client.sendData(response);

        // TODO 获取到所有群成员id，然后设置redirectMessage的to，并把消息存入kafka

//        ProtoMsg.Message redirectMessage = MessageUtil.newMessage(
//                msg.getId(),
//                sequence,
//                msg.getTo(),
//                ProtoMsg.MessageType.GROUP_MESSAGE,
//                msg.getChatMessage()
//        );
//
//        producer.sendMessage(redirectMessage);

    }

    @Override
    public Integer getType() {
        return ProtoMsg.MessageType.GROUP_MESSAGE_VALUE;
    }
}
