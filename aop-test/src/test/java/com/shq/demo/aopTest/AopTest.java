package com.shq.demo.aopTest;

import com.shq.demo.aopTest.aop.MyCalculator;
import com.shq.demo.aopTest.config.AopConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AopTest {

    @Test
    public void test01() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AopConfig.class);

        MyCalculator calculator = applicationContext.getBean("calculator", MyCalculator.class);

        calculator.div(1,1);
    }

}
