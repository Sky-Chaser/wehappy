package cn.chasers.wehappy.gateway.mq;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.gateway.ws.PushHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * Kafka消费者
 *
 * @author lollipop
 */
@Slf4j
@EnableBinding(MqSink.class)
public class Consumer {
    /**
     * 监听kafka中推送消息的事件
     *
     * @param message 推送消息
     */
    @StreamListener(MqSink.MESSAGE_INPUT)
    public void receivePushMessage(ProtoMsg.Message message) {
        log.info("message: {}", message);
        PushHandler.sendTo(message);
    }
}
