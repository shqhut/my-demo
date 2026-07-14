package com.shq.demo.common.dao;

import com.shq.demo.common.entity.User;

import java.util.List;

public interface UserDao {

    List<User> selectAll();

}
