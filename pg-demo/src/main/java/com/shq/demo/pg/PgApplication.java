package com.shq.demo.pg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shq.demo.pg.mapper")
public class PgApplication {

    public static void main(String[] args) {
        SpringApplication.run(PgApplication.class, args);
    }

}
