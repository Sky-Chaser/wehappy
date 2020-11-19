package cn.chasers.wehappy.account.mq;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * 自定义的output通道，对应kafka中的topic
 *
 * @author lollipop
 */
@Component
public interface MqSource {

    String MESSAGE_OUTPUT = "message-output";
    String PUSH_MESSAGE_OUTPUT = "push-message-output";
    String SNAP_OUTPUT = "snap-output";

    /**
     * 存储消息的通道
     *
     * @return 通道
     */
    @Output(MqSource.MESSAGE_OUTPUT)
    MessageChannel messageOutput();

    /**
     * 存储推送消息的通道
     *
     * @return 通道
     */
    @Output(MqSource.PUSH_MESSAGE_OUTPUT)
    MessageChannel pushMessageOutput();

    /**
     * 存储抢红包消息的通道
     *
     * @return 通道
     */
    @Output(MqSource.SNAP_OUTPUT)
    MessageChannel snapOutput();
}
