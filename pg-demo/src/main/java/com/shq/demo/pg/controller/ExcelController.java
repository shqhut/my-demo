package com.shq.demo.pg.controller;

import com.shq.demo.pg.domain.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);


    @RequestMapping("/uploadExcel")
    public ApiResponse uploadExcel() {
        doSomething();
        // 定义上传的Excel文件要
        return ApiResponse.success(null);
    }

    @RequestMapping("/logTest")
    public ApiResponse logTest(String orderId) {
        processOrder(orderId);
        // 定义上传的Excel文件要
        return ApiResponse.success(null);
    }

    public void doSomething() {
        logger.info("业务处理开始，用户ID: {}", "user123");

        try {
            // 模拟一个异常
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            // 正确的做法：将异常对象作为最后一个参数传入
            // 这样 Logback 才会自动记录完整的堆栈信息
            logger.error("发生算术异常，数据校验失败", e);

            // 错误做法（不会记录堆栈）：logger.error("发生算术异常：" + e.getMessage());
        }

        // 使用 MDC 注入额外上下文信息（可选，但非常有用）
        // MDC.put("requestId", UUID.randomUUID().toString());
        // logger.info("请求处理完成");
        // MDC.clear();
    }

    public void processOrder(String orderId) {
        // 设置 MDC 上下文（会自动出现在 JSON 日志中）
        MDC.put("requestId", UUID.randomUUID().toString().substring(0, 8));
        MDC.put("orderId", orderId);

        logger.info("开始处理订单");

        try {
            // 业务逻辑...
            if (orderId == null) {
                throw new IllegalArgumentException("订单ID不能为空");
            }
            logger.debug("订单处理成功，耗时: {}ms", 150);
        } catch (Exception e) {
            // 关键：必须将异常对象作为最后一个参数传入
            logger.error("订单处理失败，orderId: {}", orderId, e);
        } finally {
            // 清理 MDC，避免内存泄漏
            MDC.clear();
        }
    }

}
