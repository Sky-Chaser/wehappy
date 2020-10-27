package cn.chasers.wehappy.user.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.MimeTypeUtils;

import java.util.Map;

/**
 * Kafka生产者
 *
 * @author zhangyuanhang
 */
@Slf4j
@EnableBinding(Source.class)
public class Producer {

    private final Source source;

    @Autowired
    public Producer(Source source) {
        this.source = source;
    }

    /**
     * 向kafka中存储发送注册验证码邮件的消息
     *
     * @param message 包含邮件内容
     */
    public void sendRegisterCodeEmail(Map<String, Object> message, long expire) {
        source.output().send(MessageBuilder.withPayload(message).setExpirationDate(expire)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE).build());
    }

}