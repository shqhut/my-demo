package com.shq.demo.aopTest.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class MyAspect {

    // 定义切入点
    @Pointcut("execution(* com.shq.demo.aopTest.aop.MyCalculator.*(..))")
    public void serviceLayer() {}

    // 前置通知
    @Before("serviceLayer()")
    public void beforeAdvice(JoinPoint joinPoint) {
        System.out.println("前置通知 - 方法：" + joinPoint.getSignature().getName());
    }

    // 后置通知（无论是否异常都执行）
    @After("serviceLayer()")
    public void afterAdvice() {
        System.out.println("后置通知");
    }

    // 返回通知（方法成功执行后）
    @AfterReturning(value = "serviceLayer()", returning = "result")
    public void afterReturning(Object result) {
        System.out.println("返回通知 - 结果：" + result);
    }

}
