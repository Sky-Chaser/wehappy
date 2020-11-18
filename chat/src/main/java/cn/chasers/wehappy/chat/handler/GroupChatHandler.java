package cn.chasers.wehappy.chat.handler;

import cn.chasers.wehappy.chat.feign.IGroupService;
import cn.chasers.wehappy.chat.ws.WebSocketClient;
import cn.chasers.wehappy.common.config.SnowflakeConfig;
import cn.chasers.wehappy.chat.handler.dispatcher.MessageHandler;
import cn.chasers.wehappy.chat.mq.Producer;
import cn.chasers.wehappy.common.domain.GroupDto;
import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.common.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

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
    private final IGroupService groupService;

    @Autowired
    public GroupChatHandler(SnowflakeConfig snowflakeConfig, Producer producer, IGroupService groupService) {
        this.snowflakeConfig = snowflakeConfig;
        this.producer = producer;
        this.groupService = groupService;
    }

    @Override
    public void execute(ProtoMsg.Message msg, WebSocketClient client) {
        Long from = client.getUserId();
        Long to = Long.parseLong(msg.getTo());

        GroupDto r1 = groupService.get(to).getData();

        if (r1 == null || r1.getId() == null || r1.getId() == 0) {
            ProtoMsg.Message response = MessageUtil.newMessage(
                    msg.getId(),
                    null,
                    String.valueOf(System.currentTimeMillis()),
                    msg.getChatMessage().getFrom(),
                    ProtoMsg.MessageType.RESPONSE_MESSAGE,
                    MessageUtil.newResponseMessage(false, 0, "群组不存在", true)
            );

            client.sendData(response);
            return;
        }

        Map<String, Object> r2 = groupService.getGroupUser(to, from).getData();

        if (r2 == null || r2.size() == 0 || !r2.get("status").equals(0)) {
            ProtoMsg.Message response = MessageUtil.newMessage(
                    msg.getId(),
                    null,
                    String.valueOf(System.currentTimeMillis()),
                    msg.getChatMessage().getFrom(),
                    ProtoMsg.MessageType.RESPONSE_MESSAGE,
                    MessageUtil.newResponseMessage(false, 0, "您不是群成员", true)
            );

            client.sendData(response);
            return;
        }

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

        ProtoMsg.Message redirectMessage = MessageUtil.newMessage(
                msg.getId(),
                sequence,
                String.valueOf(System.currentTimeMillis()),
                msg.getTo(),
                ProtoMsg.MessageType.GROUP_MESSAGE,
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
        return ProtoMsg.MessageType.GROUP_MESSAGE_VALUE;
    }
}
