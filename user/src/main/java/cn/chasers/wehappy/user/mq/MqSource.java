package cn.chasers.wehappy.user.mq;


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

    String REGISTER_EMAIL_OUTPUT = "register-email-output";

    String MESSAGE_OUTPUT = "message-output";

    /**
     * 发送注册验证码邮件的通道
     *
     * @return 通道
     */
    @Output(MqSource.REGISTER_EMAIL_OUTPUT)
    MessageChannel registerEmailOutput();

    /**
     * 推送消息给客户端的通道
     *
     * @return 通道
     */
    @Output(MqSource.MESSAGE_OUTPUT)
    MessageChannel messageOutput();
}
