package cn.chasers.wehappy.message.mq;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.util.MimeTypeUtils;


/**
 * Kafka 生产者
 *
 * @author lollipop
 */
@Slf4j
@EnableBinding(MqSource.class)
public class Producer {

    private final MqSource mqSource;

    @Autowired
    public Producer(MqSource mqSource) {
        this.mqSource = mqSource;
    }

    /**
     * 向 kafka 中存储要推送给客户端的消息
     *
     * @param message 包含消息详细内容
     */
    @SendTo(MqSource.PUSH_MESSAGE_OUTPUT)
    public void sendPushMessage(ProtoMsg.Message message) {
        mqSource.pushMessageOutput().send(MessageBuilder.withPayload(message.toByteArray()).
                setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE).build());
    }
}