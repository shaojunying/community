package com.shao.community.dao;

import com.shao.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shao
 * @date 2020-08-19 21:01
 */
@Mapper
public interface UserMapper {

    /**
     * 通过id查询用户
     *
     * @param id
     * @return
     */
    User selectById(int id);

    /**
     * 通过用户名查询用户
     *
     * @param name
     * @return
     */
    User selectByName(String name);

    /**
     * 通过邮箱查询用户
     *
     * @param email
     * @return
     */
    User selectByEmail(String email);

    /**
     * 插入用户
     *
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 更新用户状态
     *
     * @param id
     * @param status 用户状态 0代表未激活,1代表激活
     * @return
     */
    int updateStatus(int id, int status);

    /**
     * 更新用户头像链接
     *
     * @param id
     * @param headerUrl
     * @return
     */
    int updateHeader(int id, String headerUrl);

    /**
     * 更新用户密码
     *
     * @param id
     * @param password
     * @return
     */
    int updatePassword(int id, String password);

}
