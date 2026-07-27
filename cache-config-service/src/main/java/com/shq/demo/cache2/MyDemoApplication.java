package com.shq.demo.cache2;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
@MapperScan("com.shq.demo.common.dao")
@EnableCaching  // 开启缓存
public class MyDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MyDemoApplication.class, args);
        String osName = applicationContext.getEnvironment().getProperty("os.name");
        System.out.println(osName);
        log.info("MyDemoApplication服务启动成功");
    }

}
