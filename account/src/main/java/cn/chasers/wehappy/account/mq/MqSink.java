package cn.chasers.wehappy.account.mq;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/**
 * 自定义的output通道，对应kafka中的topic
 *
 * @author lollipop
 */
@Component
public interface MqSink {

    String SNAP_INPUT = "snap-input";


    /**
     * 消费抢红包消息的通道
     *
     * @return 通道
     */
    @Output(MqSink.SNAP_INPUT)
    SubscribableChannel snapInput();
}
