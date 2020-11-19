package cn.chasers.wehappy.common.aspect.schedule;

import cn.chasers.wehappy.common.annotation.DisSchedule;
import cn.chasers.wehappy.common.util.RedLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.redisson.RedissonRedLock;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

/**
 * DisSchedule切面
 *
 * @author lollipop
 * @date 2020/11/19 12:38
 */
@Order(100)
@Aspect
@Slf4j
public class DisScheduleAspect {

    @Pointcut("@annotation(cn.chasers.wehappy.common.annotation.DisSchedule)")
    public void disScheduleAnnotation() {
    }

    @Around("disScheduleAnnotation() && @annotation(disSchedule)")
    public Object disSchedule(ProceedingJoinPoint pjp, DisSchedule disSchedule) throws Throwable {
        // 获取name
        String name = disSchedule.name();
        if (StringUtils.isEmpty(name)) {
            // 方法名
            Signature signature = pjp.getSignature();
            name = signature.getName();
        }

        RedissonRedLock lock = null;
        try {
            lock = RedLockUtil.tryLock(0, 2000000, name);
            if (lock == null) {
                return null;
            }

            return pjp.proceed();
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }

        return null;
    }
}
