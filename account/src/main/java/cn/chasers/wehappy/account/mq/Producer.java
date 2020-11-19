package cn.chasers.wehappy.account.mq;

import cn.chasers.wehappy.account.entity.SmallRedEnvelope;
import cn.chasers.wehappy.common.msg.ProtoMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
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
     * 向 kafka 中存储消息
     *
     * @param message 包含消息详细内容
     */
    public void sendMessage(ProtoMsg.Message message) {
        mqSource.messageOutput().send(MessageBuilder.withPayload(message).
                setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE).build());
    }

    /**
     * 向 kafka 中存储抢红包消息
     *
     * @param smallRedEnvelope 包含消息详细内容
     */
    public void sendSnap(SmallRedEnvelope smallRedEnvelope) {
        mqSource.messageOutput().send(MessageBuilder.withPayload(smallRedEnvelope)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE).build());
    }

    /**
     * 向 kafka 中存储推送消息
     *
     * @param message
     */
    public void sendPushMessage(ProtoMsg.Message message) {
        mqSource.pushMessageOutput().send(MessageBuilder.withPayload(message).
                setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE).build());
    }
}