package com.shq.demo.bct.config;

import com.shq.demo.bct.bean.Blue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.shq.demo.bct.extension")
public class ExtConfig {

    @Bean
    public Blue blue() {
        return new Blue();
    }

}
