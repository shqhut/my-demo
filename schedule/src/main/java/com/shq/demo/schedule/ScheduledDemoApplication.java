package com.shq.demo.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling   // @EnableScheduling —— 开启定时任务能力
public class ScheduledDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduledDemoApplication.class, args);
    }

}
