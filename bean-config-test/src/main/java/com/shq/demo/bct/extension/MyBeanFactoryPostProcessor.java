package com.shq.demo.bct.extension;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("MyBeanFactoryPostProcessor....postProcessBeanFactory执行");
        int beanDefinitionCount = beanFactory.getBeanDefinitionCount();
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        System.out.println("当前beanFactory中有" + beanDefinitionCount + "个beanDefinition");
        System.out.println(Arrays.asList(beanDefinitionNames));
    }

}
