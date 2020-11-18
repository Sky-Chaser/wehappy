package cn.chasers.wehappy.message.mq;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.message.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final IMessageService messageService;

    @Autowired
    public Consumer(IMessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 监听kafka中消息主题
     *
     * @param message 消息
     */
    @StreamListener(MqSink.MESSAGE_INPUT)
    public void receivePushMessage(ProtoMsg.Message message) {
        log.info("fetch message: {}", message);
        messageService.saveMessage(message);
    }
}
