package cn.chasers.wehappy.message.mq;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 自定义的output通道，对应 kafka 中的 topic
 *
 * @author lollipop
 */
public interface MqSource {
    String PUSH_MESSAGE_OUTPUT = "push-message-output";

    /**
     * 推送消息给客户端的通道
     *
     * @return 通道
     */
    @Output(PUSH_MESSAGE_OUTPUT)
    MessageChannel pushMessageOutput();
}
