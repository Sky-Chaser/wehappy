package cn.chasers.wehappy.chat.mq;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * 自定义的output通道，对应 kafka中的topic
 *
 * @author lollipop
 */
@Component
public interface MqSource {
    String MESSAGE_OUTPUT = "message-output";

    /**
     * 推送消息给客户端的通道
     *
     * @return 通道
     */
    @Output(MqSource.MESSAGE_OUTPUT)
    MessageChannel messageOutput();
}
