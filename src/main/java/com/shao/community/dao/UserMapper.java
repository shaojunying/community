package com.shao.community.dao;

import com.shao.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Author: shao
 * Date: 2020-08-19
 * Time: 21:01
 */
@Mapper
public interface UserMapper {

    User selectById(int id);

    // TODO: name和email不唯一,可能返回多个User对象
    User selectByName(String name);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);

}
