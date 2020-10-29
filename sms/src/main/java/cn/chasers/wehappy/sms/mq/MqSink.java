package cn.chasers.wehappy.sms.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/**
 * 自定义的input通道，对应kafka中的topic
 *
 * @author lollipop
 */
@Component
public interface MqSink {

    String REGISTER_EMAIL_INPUT = "register-email-input";

    /**
     * 监听发送注册验证码邮件事件的通道
     *
     * @return 通道
     */
    @Input(MqSink.REGISTER_EMAIL_INPUT)
    SubscribableChannel registerEmailInput();
}
