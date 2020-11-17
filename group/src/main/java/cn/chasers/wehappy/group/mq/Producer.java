package cn.chasers.wehappy.group.mq;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.MimeTypeUtils;

import java.util.Map;

/**
 * Kafka 生产者
 *
 * @author liamcoder
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
     * 向kafka中存储要推送给客户端的消息
     *
     * @param message 包含消息详细内容
     */
    public void sendMessage(ProtoMsg.Message message) {
        mqSource.messageOutput().send(MessageBuilder.withPayload(message).
                setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE).build());
    }

}