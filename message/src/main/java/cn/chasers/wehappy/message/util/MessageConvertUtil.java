package cn.chasers.wehappy.message.util;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.common.util.MessageUtil;
import cn.chasers.wehappy.message.entity.Message;
import cn.chasers.wehappy.message.entity.MessageIndex;

/**
 * 消息转化工具类
 *
 * @author lollipop
 * @date 2020/11/18 12:24:55
 */
public class MessageConvertUtil {

    /**
     * 将 proto.Message 类型转化为 Message 类型
     *
     * @param msg proto.Message
     * @return
     */
    public static Message proto2db(ProtoMsg.Message msg) {
        Message message = new Message();
        message.setId(Long.parseLong(msg.getSequence()));
        message.setTime(Long.parseLong(msg.getTime()));
        message.setType(msg.getMessageTypeValue());

        if (msg.getMessageType() == ProtoMsg.MessageType.PUSH_MESSAGE) {
            message.setContent(msg.getPushMessage().getContent());
        } else if (msg.getMessageType() == ProtoMsg.MessageType.SINGLE_MESSAGE || msg.getMessageType() == ProtoMsg.MessageType.GROUP_MESSAGE) {
            message.setContent(msg.getChatMessage().getContent());
        }

        return message;
    }

    /**
     * 将 proto.Message 类型转化为 Message 类型
     *
     * @param message      Message
     * @param messageIndex MessageIndex
     * @return
     */
    public static ProtoMsg.Message db2proto(Message message, MessageIndex messageIndex) {
        Object msg = null;
        String to = messageIndex.getTo().toString();
        if (message.getType() == ProtoMsg.MessageType.PUSH_MESSAGE_VALUE) {
            msg = MessageUtil.newPushMessage(ProtoMsg.ContentType.forNumber(message.getType()), message.getContent());
        } else if (message.getType() == ProtoMsg.MessageType.SINGLE_MESSAGE_VALUE || message.getType() == ProtoMsg.MessageType.GROUP_MESSAGE_VALUE) {
            msg = MessageUtil.newChatMessage(to, messageIndex.getFrom().toString(), ProtoMsg.ContentType.forNumber(message.getType()), message.getContent());
        }

        return MessageUtil.newMessage(message.getId().toString(), message.getId().toString(), message.getTime().toString(), to, ProtoMsg.MessageType.forNumber(message.getType()), msg);
    }
}
