package com.shq.demo.cache2.controller;

import com.shq.demo.cache2.service.UserService;
import com.shq.demo.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Spring Boot 2.4!";
    }

    @GetMapping("/getUserList")
    public List<User> getUserList() {
        return userService.selectAll();
    }

}
