package com.shq.demo.bct;

import com.shq.demo.bct.config.MyConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

public class ConditionTest {

    ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyConfig.class);

    @Test
    public void testCondition() {
        Environment environment = applicationContext.getEnvironment();
        String systemOs = environment.getProperty("os.name");
        String systemOs2 = environment.getProperty("user.country");
        System.out.println("当前操作系统为：" + systemOs);
        System.out.println("当前操作系统为：" + systemOs2);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanName:beanDefinitionNames) {
            System.out.println(beanName);
        }
    }

}
