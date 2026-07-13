package com.shq.demo.service.impl;

import com.shq.demo.dao.UserDao;
import com.shq.demo.entity.User;
import com.shq.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    @Cacheable(value = "user", key = "'all'")
    public List<User> selectAll() {
        return userDao.selectAll();
    }
}
