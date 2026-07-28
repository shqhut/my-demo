package com.shq.demo.base64.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
public class AppConfig {

    @Value("${app.photo.multi-thread-enabled}")
    private boolean multiThreadEnabled;

    @Value("${app.photo.thread-pool-size:10}")
    private int corePoolSize;

    @Value("${app.photo.max-pool-size:20}")
    private int maxPoolSize;

    @Value("${app.photo.queue-capacity:5000}")
    private int queueCapacity;

    /**
     * 配置线程池
     */
    @Bean(name = "photoExtractExecutor")
    public Executor photoExtractExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数
        executor.setCorePoolSize(multiThreadEnabled ? corePoolSize : 1);

        // 最大线程数
        executor.setMaxPoolSize(multiThreadEnabled ? maxPoolSize : 1);

        // 队列容量 - 设置足够大，避免任务被拒绝
        executor.setQueueCapacity(queueCapacity);

        // 线程名前缀
        executor.setThreadNamePrefix("photo-extract-");

        // 等待所有任务完成再关闭
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(120);

        // 拒绝策略：由调用者线程执行（不会丢失任务）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 允许核心线程超时
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(60);

        executor.initialize();
        return executor;
    }
}