package com.shao.community.service;

import com.shao.community.CommunityApplication;
import com.shao.community.entity.User;
import com.shao.community.util.CommunityConstant;
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

    @Test
    @Transactional
    public void activate() {

        int result1 = userService.activate(138, "69dcd69f4c0145058df820e90820ba1e");
        Assert.assertEquals(result1, CommunityConstant.ACTIVATION_REPEAT);

        int result2 = userService.activate(132112, "69dcd69f4c0145058df820e9");
        Assert.assertEquals(result2, CommunityConstant.ACTIVATION_FAILURE);

        int result3 = userService.activate(168, "18d034086689456e8ac263c8ef888425");
        Assert.assertEquals(result3, CommunityConstant.ACTIVATION_SUCCESS);
    }

    @Test
    @Transactional
    public void loginWithNullUsername() {
        // 空用户名登录
        Map<String, Object> map = userService.login(null, "password", 10);
        Assert.assertTrue(map.containsKey("usernameMsg"));
        Assert.assertEquals(map.get("usernameMsg"), "用户名不能为空");
    }

    @Test
    @Transactional
    public void loginWithNullPassword() {
        // 空密码登录
        Map<String, Object> map = userService.login("username", null, 10);
        Assert.assertTrue(map.containsKey("passwordMsg"));
        Assert.assertEquals(map.get("passwordMsg"), "密码不能为空");
    }

    @Test
    @Transactional
    public void loginWithNoExistUsername() {
        // 不存在的用户名
        Map<String, Object> map = userService.login("username", "igLLZdM4yMiqC8i", 10);
        Assert.assertTrue(map.containsKey("usernameMsg"));
        Assert.assertEquals(map.get("usernameMsg"), "用户名不存在");
    }

    @Test
    @Transactional
    public void loginWithInactiveAccount() {
        // 未激活的账号
        Map<String, Object> map = userService.login("shaojunying1", "igLLZdM4yMiqC8i", 10);
        Assert.assertTrue(map.containsKey("usernameMsg"));
        Assert.assertEquals(map.get("usernameMsg"), "该账号未激活");
    }

    @Test
    @Transactional
    public void loginWithWrongPassword() {
        Map<String, Object> map = userService.login("shaojunying111", "TnsrkRPKbaFb33d1", 10);
        Assert.assertTrue(map.containsKey("passwordMsg"));
        Assert.assertEquals(map.get("passwordMsg"), "密码错误，请重新输入");
    }

    @Test
    @Transactional
    public void loginWithNormalInformation() {
        Map<String, Object> map = userService.login("shaojunying111", "TnsrkRPKbaFb33d", 10);
        Assert.assertTrue(map.containsKey("ticket"));
    }
}
