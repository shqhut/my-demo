package com.shq.demo.aopTest.config;

import com.shq.demo.aopTest.aop.MyAspect;
import com.shq.demo.aopTest.aop.MyCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAspectJAutoProxy
@Import(MyAspect.class)
public class AopConfig {

    @Bean
    public MyCalculator calculator() {
        return new MyCalculator();
    }

}
