package com.shq.demo.cache.service.impl;

import com.shq.demo.cache.service.UserService;
import com.shq.demo.common.dao.UserDao;
import com.shq.demo.common.entity.User;
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
