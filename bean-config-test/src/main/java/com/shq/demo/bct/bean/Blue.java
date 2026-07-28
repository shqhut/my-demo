package com.shq.demo.bct.bean;

import javax.annotation.PostConstruct;

public class Blue {

    public Blue() {
        System.out.println("Blue开始创建了....");
    }

    @PostConstruct
    public void init() {
        System.out.println("执行Blue...init()...");
    }


}
