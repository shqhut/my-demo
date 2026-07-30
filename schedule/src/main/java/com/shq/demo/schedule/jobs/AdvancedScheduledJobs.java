package com.shq.demo.schedule.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 进阶定时任务示例
 *
 * 演示：
 *   1. @Async 异步执行（任务丢到线程池，调度线程不阻塞）
 *   2. 动态 Cron 表达式（运行时通过接口修改）
 *   3. 任务执行状态追踪（计数、耗时统计）
 *   4. 幂等性保障思路
 */
//@Component
//@EnableAsync
public class AdvancedScheduledJobs {

    private static final Logger log = LoggerFactory.getLogger(AdvancedScheduledJobs.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    /** 任务执行计数器 */
    private final AtomicInteger taskCounter = new AtomicInteger(0);

    // ==================== 1. @Async 异步任务 ====================

    /**
     * @Async + @Scheduled 组合：
     *   调度线程只负责"触发"，任务逻辑丢到异步线程池执行。
     *
     * ✅ 适合：任务耗时较长（如调用外部 API、大批量数据处理），
     *         避免阻塞调度线程，不影响其他定时任务。
     *
     * ⚠️ 注意：需要在配置类里声明 Executor bean，否则用默认 SimpleAsyncTaskExecutor
     */
    @Async("taskExecutor")
    @Scheduled(fixedRate = 5000)
    public void asyncTask() {
        int seq = taskCounter.incrementAndGet();
        long start = System.currentTimeMillis();
        log.info("[async     ] #{} 开始, 线程: {}", seq, Thread.currentThread().getName());

        // 模拟耗时操作
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long cost = System.currentTimeMillis() - start;
        log.info("[async     ] #{} 完成, 耗时: {}ms", seq, cost);
    }

    // ==================== 2. 带锁的幂等任务 ====================

    /**
     * 模拟"多实例部署时防止重复执行"的简易方案：
     *   用 JVM 内存锁（生产环境换成 Redis 分布式锁，即 ShedLock 方案）
     *
     * 场景：订单超时关闭、消息重投等，重复执行会导致业务异常
     */
    private volatile boolean jobLocked = false;

    @Scheduled(fixedRate = 4000)
    public void idempotentTask() {
        if (jobLocked) {
            log.warn("[idempotent] 上一次还没跑完，本次跳过");
            return;
        }
        try {
            jobLocked = true;
            log.info("[idempotent] 执行中... 线程: {}", Thread.currentThread().getName());
            // 模拟业务
            Thread.sleep(1000);
        } catch (Exception e) {
            log.error("[idempotent] 执行异常", e);
        } finally {
            jobLocked = false;
        }
    }

    // ==================== 3. 任务执行统计 ====================

    /**
     * 定期打印统计信息（每 30 秒）
     */
    @Scheduled(fixedRate = 30000)
    public void printStats() {
        log.info("========== 任务统计 ==========");
        log.info("异步任务累计执行: {} 次", taskCounter.get());
        log.info("当前时间: {}", LocalDateTime.now().format(FMT));
        log.info("==============================");
    }
}
