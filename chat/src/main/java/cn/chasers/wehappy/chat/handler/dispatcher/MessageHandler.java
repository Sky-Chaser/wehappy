package cn.chasers.wehappy.chat.handler.dispatcher;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import io.netty.channel.Channel;

/**
 * @author lollipop
 * @date 2020/11/05 11:17:45
 */
public interface MessageHandler {

    /**
     * 执行消息处理
     *
     * @param channel 消息通道
     * @param msg     消息
     */
    void execute(Channel channel, ProtoMsg.Message msg);

    /**
     * 处理的消息类型
     *
     * @return Protobuf中定义的消息类型
     */
    Integer getType();
}
