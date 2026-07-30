package com.shq.demo.schedule.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 动态 Cron 任务
 *
 * 痛点：@Scheduled(cron="...") 是写死的，改了要重启。
 *
 * 解决方案：实现 SchedulingConfigurer，手动注册任务，
 *          运行时可通过接口动态修改 Cron 表达式。
 */
// 使用方式： GET http://localhost:8080/api/cron/update?expression=*/5 * * * * ?
//@Component
//@RestController
public class DynamicCronJob implements SchedulingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(DynamicCronJob.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    /** 当前生效的 Cron 表达式（可被接口动态修改） */
    private volatile String currentCron = "*/15 * * * * ?";

    // ==================== 动态注册任务 ====================

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        // 注册一个用 Trigger 动态决定下一次执行时间的任务
        registrar.addTriggerTask(
                this::doWork,          // 任务逻辑
                this::nextExecutionTime // 触发器：每次执行完调用，决定下次何时跑
        );
    }

    /**
     * Trigger 实现：每次执行后，用【最新的 currentCron】计算下次触发时间
     */
    private Date nextExecutionTime(TriggerContext triggerContext) {
        CronTrigger trigger = new CronTrigger(currentCron);
        return trigger.nextExecutionTime(triggerContext);
    }

    /**
     * 实际任务逻辑
     */
    private void doWork() {
        log.info("[dynamic-cron] 执行! cron={}, 时间={}, 线程={}",
                currentCron, LocalDateTime.now().format(FMT),
                Thread.currentThread().getName());
    }

    // ==================== 对外接口 ====================

    /**
     * 动态修改 Cron 表达式（无需重启）
     *
     * 示例：
     *
     */
    // curl "http://localhost:8080/api/cron/update?expression=*/5 * * * * ?"
    @GetMapping("/api/cron/update")
    public String updateCron(@RequestParam("expression") String expression) {
        // 简单校验：尝试解析
        try {
            new CronTrigger(expression);
        } catch (Exception e) {
            return "❌ Cron 表达式格式错误: " + expression + "，错误: " + e.getMessage();
        }
        this.currentCron = expression;
        log.info("✅ Cron 表达式已更新为: {}", expression);
        return "✅ Cron 已更新为: " + expression;
    }

    /**
     * 查看当前 Cron
     */
    @GetMapping("/api/cron/current")
    public String currentCron() {
        return "当前 Cron: " + currentCron;
    }
}
