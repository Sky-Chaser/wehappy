package cn.chasers.wehappy.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在方法执行之前，决定当前是否需要执行定时调度任务
 *
 * @author lollipop
 * @date 2020/11/19 13:29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DisSchedule {
    /**
     * 定时调度任务的名称(默认是方法名)
     */
    String name() default "";

    /**
     * 获取锁时的等待时间
     */
    long waitTime() default 1000;

    /**
     * 持有锁的时间
     */

    long releaseTime() default 5000;
}
