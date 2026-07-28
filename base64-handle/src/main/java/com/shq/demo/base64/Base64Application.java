package com.shq.demo.base64;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Base64Application {

    public static void main(String[] args) {
        SpringApplication.run(Base64Application.class, args);
        log.info("Base64Application服务启动成功");
    }

}