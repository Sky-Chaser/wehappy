package cn.chasers.wehappy.message.schedule;

import cn.chasers.wehappy.common.annotation.DisSchedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时清除消息
 *
 * @author lollipop
 * @date 2020/11/19 13:51:14
 */
@Slf4j
public class ClearExpireMessage {
    /**
     * 每小时执行一次删除过期消息数据的分布式定时任务
     */
    @DisSchedule
    @Scheduled(cron = "0 0 */1 * * ?")
    public void clearExpireMessage() {
        log.info("clearExpireMessage");
    }
}
