package cn.chasers.wehappy.gateway.mq;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.gateway.ws.PushHandler;
import com.google.protobuf.InvalidProtocolBufferException;
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

    private final PushHandler pushHandler;

    @Autowired
    public Consumer(PushHandler pushHandler) {
        this.pushHandler = pushHandler;
    }

    /**
     * 监听kafka中推送消息的事件
     *
     * @param bytes 推送消息
     */
    @StreamListener(MqSink.PUSH_MESSAGE_INPUT)
    public void receivePushMessage(byte[] bytes) {
        try {
            ProtoMsg.Message message = ProtoMsg.Message.parseFrom(bytes);
            log.info("message: {}", message);
            pushHandler.sendTo(message);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
