package cn.chasers.wehappy.group.mq;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * 自定义的output通道，对应kafka中的topic
 *
 * @author liamcoder
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
