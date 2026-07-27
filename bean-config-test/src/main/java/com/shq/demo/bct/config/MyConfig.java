package com.shq.demo.bct.config;

import com.shq.demo.bct.bean.Person;
import com.shq.demo.bct.condition.LinuxCondition;
import com.shq.demo.bct.condition.WindowsCondition;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

@Configurable
public class MyConfig {

    @Bean("Linux")
    @Conditional(LinuxCondition.class)
    public Person personLinux() {
        return new Person("Linux",50);
    }

    @Bean("Bill")
    @Conditional(WindowsCondition.class)
    public Person personBill() {
        return new Person("Bill",50);
    }

}
