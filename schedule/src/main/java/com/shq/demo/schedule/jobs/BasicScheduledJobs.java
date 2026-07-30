package com.shq.demo.schedule.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 基础定时任务示例
 *
 * 演示 @Scheduled 的三种核心用法：
 *   1. fixedDelay  —— 上一次执行结束后，延迟 N 毫秒再执行
 *   2. fixedRate   —— 按固定频率执行（不管上一次是否结束）
 *   3. cron        —— Cron 表达式，最灵活
 */
@Component
public class BasicScheduledJobs {

    private static final Logger log = LoggerFactory.getLogger(BasicScheduledJobs.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    // ==================== 1. fixedDelay ====================

    /**
     * fixedDelay：上一次任务【执行完毕】后，等待 3 秒再执行下一次。
     *
     * ✅ 适合：任务执行时间不稳定，但必须"一个接一个"串行处理的场景
     *    （如：每次处理一批数据，确保上一批完全入库后再拉下一批）
     *
     * ⚠️ 如果任务本身耗时 2 秒，那实际间隔 = 2秒(执行) + 3秒(延迟) = 5秒
     */
//    @Scheduled(fixedDelay = 3000)
    public void fixedDelayTask() {
        log.info("[fixedDelay] 执行时间: {}, 线程: {}",
                LocalDateTime.now().format(FMT), Thread.currentThread().getName());
        // 模拟业务耗时
        sleep(500);
    }

    // ==================== 2. fixedRate ====================

    /**
     * fixedRate：每 2 秒触发一次，【不管上一次是否执行完】。
     *
     * ✅ 适合：定时采集、心跳上报等"到点就跑"的场景
     *
     * ⚠️ 如果任务耗时超过 2 秒，下次触发会排队（取决于线程池是否有空闲线程）。
     *    如果线程池满了，任务会被拒绝或延迟。
     */
//    @Scheduled(fixedRate = 2000)
    public void fixedRateTask() {
        log.info("[fixedRate ] 执行时间: {}, 线程: {}",
                LocalDateTime.now().format(FMT), Thread.currentThread().getName());
        sleep(500);
    }

    /**
     * fixedRate + initialDelay：首次延迟 5 秒后开始，之后每 2 秒执行一次。
     *
     * ✅ 适合：应用启动后需要等初始化完成（如缓存预热、连接池就绪）再开始调度
     */
//    @Scheduled(initialDelay = 5000, fixedRate = 2000)
    public void fixedRateWithInitialDelayTask() {
        log.info("[fixedRate+init] 执行时间: {}, 线程: {}",
                LocalDateTime.now().format(FMT), Thread.currentThread().getName());
    }

    // ==================== 3. Cron 表达式 ====================

    /**
     * Cron 表达式：秒 分 时 日 月 周 [年(可选)]
     *
     * 示例：每 10 秒执行一次
     *
     */
    // "*/10 * * * * ?"  → 秒:每10秒, 分:每分, 时:每时, 日:每日, 月:每月, 周:不限定
    @Scheduled(cron = "*/5 * * * * ?")
    public void cronTask() {
        UUID uuid = UUID.randomUUID();
        log.info("当前任务编码为：{}", uuid);
        log.info("[cron      ] 执行时间: {}, 线程: {}",
                LocalDateTime.now().format(FMT), Thread.currentThread().getName());
        log.info("模拟定时任务耗时，线程: {}", Thread.currentThread().getName());
        sleep(6000);
        log.info("任务执行完成，编码为：{}",uuid);
    }

    /**
     * Cron 从配置文件读取（推荐！）
     *
     * 好处：改 cron 不用重新打包，改 yml 重启即可（配合 @RefreshScope 可热刷新）
     */
//    @Scheduled(cron = "${task.simple-cron}")
    public void cronFromConfigTask() {
        log.info("[cron-config] 执行时间: {}, 线程: {}",
                LocalDateTime.now().format(FMT), Thread.currentThread().getName());
    }

    /**
     * 每天凌晨 2 点执行（典型日切/对账场景）
     * "0 0 2 * * ?" → 秒0 分0 时2 每天
     */
//    @Scheduled(cron = "${task.daily-cron}")
    public void dailyTask() {
        log.info("[daily     ] 凌晨2点日切任务执行! 时间: {}",
                LocalDateTime.now().format(FMT));
    }

    // ==================== 工具方法 ====================

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
