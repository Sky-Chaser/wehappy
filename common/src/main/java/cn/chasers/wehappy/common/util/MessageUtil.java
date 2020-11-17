package cn.chasers.wehappy.common.util;


import cn.chasers.wehappy.common.msg.ProtoMsg;

/**
 * 生成 Proto.Message 类对象的工具类
 *
 * @author zhangyuanhang
 * @date 2020/11/17: 8:32 下午
 */

public class MessageUtil {
    public static ProtoMsg.Message newMessage(String id, String sequence, String time, String to, ProtoMsg.MessageType messageType, Object msg) {
        ProtoMsg.Message.Builder builder = ProtoMsg.Message.newBuilder()
                .setId(id)
                .setSequence(sequence)
                .setTime(time)
                .setMessageType(messageType)
                .setTo(to);

        if (messageType.equals(ProtoMsg.MessageType.GROUP_MESSAGE) || messageType.equals(ProtoMsg.MessageType.SINGLE_MESSAGE)) {
            return builder.setChatMessage((ProtoMsg.ChatMessage) msg).build();
        }

        if (messageType.equals(ProtoMsg.MessageType.PUSH_MESSAGE)) {
            return builder.setPushMessage((ProtoMsg.PushMessage) msg).build();
        }

        if (messageType.equals(ProtoMsg.MessageType.RESPONSE_MESSAGE)) {
            return builder.setResponseMessage((ProtoMsg.ResponseMessage) msg).build();
        }

        return builder.build();
    }

    public static ProtoMsg.ResponseMessage newResponseMessage(boolean result, int code, String info, boolean expose) {

        ProtoMsg.ResponseMessage.Builder builder = ProtoMsg.ResponseMessage.newBuilder()
                .setResult(result)
                .setCode(code)
                .setExpose(expose);

        if (info != null) {
            builder.setInfo(info);
        }

        return builder.build();
    }

    public static ProtoMsg.PushMessage newPushMessage(ProtoMsg.ContentType contentType, String content) {
        return ProtoMsg.PushMessage.newBuilder()
                .setContentType(contentType)
                .setContent(content)
                .build();
    }

    public static ProtoMsg.ChatMessage newChatMessage(String to, String from, ProtoMsg.ContentType contentType, String content) {
        return ProtoMsg.ChatMessage.newBuilder()
                .setFrom(from)
                .setTo(to)
                .setContentType(contentType)
                .setContent(content)
                .build();
    }
}
