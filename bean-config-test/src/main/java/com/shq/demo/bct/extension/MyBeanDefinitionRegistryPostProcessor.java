package com.shq.demo.bct.extension;

import com.shq.demo.bct.bean.Blue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        int beanDefinitionCount = registry.getBeanDefinitionCount();
        System.out.println("MyBeanDefinitionRegistryPostProcessor....postProcessBeanDefinitionRegistry...当前容器内beanDefinitionCount" + beanDefinitionCount);
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Blue.class);
        registry.registerBeanDefinition("myBlue",beanDefinitionBuilder.getBeanDefinition());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        int beanDefinitionCount = beanFactory.getBeanDefinitionCount();
        System.out.println("MyBeanDefinitionRegistryPostProcessor...postProcessBeanFactory...当前容器内beanDefinitionCount" + beanDefinitionCount);

    }
}
