package com.shq.demo;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@Slf4j
@MapperScan("com.shq.demo.dao")
@EnableCaching  // 开启缓存
public class MyDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyDemoApplication.class, args);
        log.info("MyDemoApplication服务启动成功");
    }

}
