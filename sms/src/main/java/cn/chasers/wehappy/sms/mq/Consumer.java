package cn.chasers.wehappy.sms.mq;

import cn.chasers.wehappy.sms.service.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.util.Map;

/**
 * Kafka消费者
 *
 * @author zhangyuanhang
 */
@Slf4j
@EnableBinding(MqSink.class)
public class Consumer {

    private final IMailService mailService;

    @Autowired
    public Consumer(IMailService mailService) {
        this.mailService = mailService;
    }

    /**
     * 监听kafka中发送注册验证码邮件的事件
     *
     * @param message 包含邮件内容
     */
    @StreamListener(MqSink.REGISTER_EMAIL_INPUT)
    public void receiveRegisterCodeEmail(Map<String, Object> message) {
        log.info("message: {}", message);
        mailService.sendRegisterCode(((String) message.get("to")), (String) message.get("code"));
    }
}
