package cn.chasers.wehappy.message.mq;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/**
 * 自定义的 input 通道，对应 kafka 中的 topic
 *
 * @author lollipop
 */
@Component
public interface MqSink {

    String MESSAGE_INPUT = "message-input";

    /**
     * 监听推送消息的通道
     *
     * @return 通道
     */
    @Input(MqSink.MESSAGE_INPUT)
    SubscribableChannel messageInput();
}
