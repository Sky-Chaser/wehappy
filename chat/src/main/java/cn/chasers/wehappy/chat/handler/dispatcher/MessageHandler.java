package cn.chasers.wehappy.chat.handler.dispatcher;

import cn.chasers.wehappy.chat.ws.WebSocketClient;
import cn.chasers.wehappy.common.msg.ProtoMsg;

/**
 * @author lollipop
 * @date 2020/11/05 11:17:45
 */
public interface MessageHandler {

    /**
     * 执行消息处理
     *
     * @param client websocket client
     * @param msg     消息
     */
    void execute(ProtoMsg.Message msg, WebSocketClient client);

    /**
     * 处理的消息类型
     *
     * @return Protobuf中定义的消息类型
     */
    Integer getType();
}
