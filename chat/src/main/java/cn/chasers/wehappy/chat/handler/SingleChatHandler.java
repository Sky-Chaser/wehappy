package cn.chasers.wehappy.chat.handler;

import cn.chasers.wehappy.chat.feign.IUserService;
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
    private final IUserService userService;

    @Autowired
    public SingleChatHandler(SnowflakeConfig snowflakeConfig, Producer producer, IUserService userService) {
        this.snowflakeConfig = snowflakeConfig;
        this.producer = producer;
        this.userService = userService;
    }

    @Override
    public void execute(ProtoMsg.Message msg, WebSocketClient client) {
        Long from = client.getUserId();
        Long to = Long.parseLong(msg.getTo());

        try {
            if (!userService.isFriend(from, to).getData()) {
                ProtoMsg.Message response = MessageUtil.newMessage(
                        msg.getId(),
                        "",
                        String.valueOf(System.currentTimeMillis()),
                        from.toString(),
                        ProtoMsg.MessageType.RESPONSE_MESSAGE,
                        MessageUtil.newResponseMessage(false, 1, "他不是你的好友", true)
                );

                client.sendData(response);
                return;
            }
        } catch (Exception e) {
            log.error("", e);
        }

        String sequence = String.valueOf(snowflakeConfig.snowflakeId());

        ProtoMsg.Message response = MessageUtil.newMessage(
                msg.getId(),
                sequence,
                String.valueOf(System.currentTimeMillis()),
                from.toString(),
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
                        from.toString(),
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
