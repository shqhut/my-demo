package com.shq.demo.demo.cache2.service.impl;

import com.shq.demo.common.dao.UserDao;
import com.shq.demo.common.entity.User;
import com.shq.demo.demo.cache2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public List<User> selectAll() {
        return userDao.selectAll();
    }

}
