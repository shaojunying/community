package com.shao.community.service;

import com.shao.community.CommunityApplication;
import com.shao.community.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Author: shao
 * Date: 2020-09-05
 * Time: 21:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @Transactional
    public void registerWithEmptyUsername() {
        User user = new User();
        user.setPassword("6236325");
        user.setEmail("test@gamil.com");
        Map<String, Object> map = userService.register(user);
        Assert.assertEquals(map.get("usernameMsg"), "用户名不能为空");
    }

    @Test
    @Transactional
    public void registerWithEmptyPassword() {
        User user = new User();
        user.setUsername("testtest");
        user.setEmail("test@gamil.com");
        Map<String, Object> map = userService.register(user);
        Assert.assertEquals(map.get("passwordMsg"), "密码不能为空");
    }

    @Test
    @Transactional
    public void registerWithEmptyEmail() {
        User user = new User();
        user.setUsername("testtest");
        user.setPassword("6236325");
        Map<String, Object> map = userService.register(user);
        Assert.assertEquals(map.get("emailMsg"), "邮箱不能为空");
    }

    @Test
    @Transactional
    public void registerWithExistedUsername() {
        User user = new User();
        user.setUsername("liubei");
        user.setPassword("shaoe22663452341524215");
        user.setEmail("shaojunying1@gmail.com");

        Map<String, Object> map = userService.register(user);
        Assert.assertEquals(map.get("usernameMsg"), "用户名已存在");
    }

    @Test
    @Transactional
    public void registerWithExistedEmail() {
        User user = new User();
        user.setUsername("shaoshaoshao");
        user.setPassword("shaoe22663452341524215");
        user.setEmail("nowcoder102@sina.com");

        Map<String, Object> map = userService.register(user);
        Assert.assertEquals(map.get("emailMsg"), "邮箱已存在");
    }


    @Test
    @Transactional
    public void registerWithNormalUsername() {
        User user = new User();
        user.setUsername("shaojunying");
        user.setPassword("shaoe22663452341524215");
        user.setEmail("shaojunying1@gmail.com");

        Map<String, Object> map = userService.register(user);
        Assert.assertTrue(map.isEmpty());
    }

}
