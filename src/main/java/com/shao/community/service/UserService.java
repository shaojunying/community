package com.shao.community.service;

import com.shao.community.dao.UserMapper;
import com.shao.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: shao
 * Date: 2020-09-02
 * Time: 21:35
 */
@Service
public class UserService {

    @Autowired
    private UserMapper mapper;

    public User findUserById(int id) {
        return mapper.selectById(id);
    }

}
