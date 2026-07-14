package com.shq.demo.dao;

import com.shq.demo.entity.User;

import java.util.List;

public interface UserDao {

    List<User> selectAll();

}
