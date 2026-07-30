package com.shq.demo.schedule.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 手动触发 + 定时触发 双模式任务
 *
 * 场景：有些任务平时定时跑，但运维有时需要手动立即执行一次。
 *       把业务逻辑抽成独立方法，定时和接口共用。
 */
//@Component
//@RestController
//@RequestMapping("/api/job")
public class ManualTriggerJob {

    private static final Logger log = LoggerFactory.getLogger(ManualTriggerJob.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * 核心业务逻辑 —— 定时和手动共用
     */
    public String doBusinessLogic() {
        String now = LocalDateTime.now().format(FMT);
        log.info("[manual-job] 业务逻辑执行，时间: {}", now);
        // 这里写你的真实业务：发邮件、清缓存、拉数据……
        return "任务执行成功，时间: " + now;
    }

    // ==================== 定时触发 ====================

    /**
     * 每 30 秒自动执行
     */
    @Scheduled(fixedRate = 30000)
    public void scheduledRun() {
        log.info("[manual-job] 定时触发");
        doBusinessLogic();
    }

    // ==================== 手动触发接口 ====================

    @GetMapping("/run")
    public String manualRun() {
        log.info("[manual-job] 手动触发");
        return doBusinessLogic();
    }

    @GetMapping("/status")
    public String status() {
        return "ManualTriggerJob 运行中，当前时间: " + LocalDateTime.now().format(FMT);
    }
}
