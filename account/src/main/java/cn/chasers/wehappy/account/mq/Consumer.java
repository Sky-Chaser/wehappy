package cn.chasers.wehappy.account.mq;

import cn.chasers.wehappy.account.entity.SmallRedEnvelope;
import cn.chasers.wehappy.account.service.IBigRedEnvelopeService;
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
    private final IBigRedEnvelopeService bigRedEnvelopeService;

    @Autowired
    public Consumer(IBigRedEnvelopeService bigRedEnvelopeService) {
        this.bigRedEnvelopeService = bigRedEnvelopeService;
    }

    /**
     * 监听kafka中抢红包的消息
     *
     * @param smallRedEnvelope 抢红包消息
     */
    @StreamListener(MqSink.SNAP_INPUT)
    public void receiveSnapMessage(SmallRedEnvelope smallRedEnvelope) {
        log.info("smallRedEnvelope~: {}", smallRedEnvelope);
        bigRedEnvelopeService.doSnap(smallRedEnvelope);
    }
}
