package cn.chasers.wehappy.account.schedule;

import cn.chasers.wehappy.account.service.IBigRedEnvelopeService;
import cn.chasers.wehappy.common.annotation.DisSchedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时清除消息
 *
 * @author lollipop
 * @date 2020/11/19 13:51:14
 */
@Slf4j
@Component
public class ScanRedEnvelope {
    private final IBigRedEnvelopeService bigRedEnvelopeService;

    @Autowired
    public ScanRedEnvelope(IBigRedEnvelopeService bigRedEnvelopeService) {
        this.bigRedEnvelopeService = bigRedEnvelopeService;
    }

    /**
     * 每十分钟执行一次退回红包的分布式定时任务
     */
    @DisSchedule
    @Scheduled(cron = "0 */10 * * * ?")
    public void sendBack() {
        log.info("sendBack");
        bigRedEnvelopeService.sendBack();
    }
}
