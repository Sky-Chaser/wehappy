package cn.chasers.wehappy.message.util;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.common.util.MessageUtil;
import cn.chasers.wehappy.message.entity.Message;

/**
 * 消息转化工具类
 *
 * @author lollipop
 * @date 2020/11/18 12:24:55
 */
public class MessageConvertUtil {

    public static Message proto2db(ProtoMsg.Message msg) {
        Message message = new Message();
        message.setSequence(Long.parseLong(msg.getSequence()));
        message.setTime(Long.parseLong(msg.getTime()));
        message.setType(msg.getMessageTypeValue());

        if (msg.getMessageType() == ProtoMsg.MessageType.PUSH_MESSAGE) {
            message.setContent(msg.getPushMessage().getContent());
        } else if (msg.getMessageType() == ProtoMsg.MessageType.SINGLE_MESSAGE || msg.getMessageType() == ProtoMsg.MessageType.GROUP_MESSAGE) {
            message.setContent(msg.getChatMessage().getContent());
        }

        return message;
    }

//    public static Message db2proto(Message message, Long to) {
//        Object msg;
//        if (message.getType() == ProtoMsg.MessageType.PUSH_MESSAGE_VALUE) {
//            msg = MessageUtil.newPushMessage(message)
//        }
//        MessageUtil.newMessage(message.getId(), message.getSequence().toString(), message.getTime().toString(), to.toString(), message.getType());
//
//        Message message = new Message();
//        message.setSequence(Long.parseLong(msg.getSequence()));
//        message.setTime(Long.parseLong(msg.getTime()));
//        message.setType(msg.getMessageTypeValue());
//
//        if (msg.getMessageType() == ProtoMsg.MessageType.PUSH_MESSAGE) {
//            message.setContent(msg.getPushMessage().getContent());
//        } else if (msg.getMessageType() == ProtoMsg.MessageType.SINGLE_MESSAGE || msg.getMessageType() == ProtoMsg.MessageType.GROUP_MESSAGE) {
//            message.setContent(msg.getChatMessage().getContent());
//        }
//
//        return message;
//    }
}
