package com.shq.demo.schedule.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 定时任务线程池配置
 *
 * 为什么要自定义线程池？
 * --------------------
 * Spring @Scheduled 默认使用单线程调度器（SimpleAsyncTaskExecutor 的变体），
 * 所有任务串行执行。如果一个任务阻塞/耗时过长，后续任务全部排队延迟。
 *
 * 通过 SchedulingConfigurer 自定义线程池，让多个任务并行执行、互不干扰。
 */
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(SchedulerConfig.class);

    /**
     * 方式一：配置 TaskScheduler（推荐）
     * 影响所有 @Scheduled 注解的任务
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);                    // 核心线程数：根据任务数量调整
        scheduler.setThreadNamePrefix("scheduled-");  // 线程名前缀，方便排查日志
        scheduler.setAwaitTerminationSeconds(60);     // 优雅停机等待时间
        scheduler.setWaitForTasksToCompleteOnShutdown(true); // 停机时等待任务完成
        scheduler.setErrorHandler(t -> log.error("定时任务执行异常", t)); // 全局异常捕获
        return scheduler;
    }

    /**
     * 方式二：通过 SchedulingConfigurer 配置（更底层，可精细控制）
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        // 设置自定义线程池
        registrar.setTaskScheduler(taskScheduler());
    }
}
