package com.shao.community.dao;

import com.shao.community.CommunityApplication;
import com.shao.community.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Author: shao
 * Date: 2020-08-19
 * Time: 21:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void insertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setSalt("test");
        user.setActivationCode("code");
        user.setEmail("1@gmail.com");
        int i = userMapper.insertUser(user);
        // insert返回值是该操作影响的行数
        Assert.assertEquals(i, 1);
        Assert.assertNotEquals(user.getId(), 0);
    }


    @Test
    public void selectByName() {
        User user = userMapper.selectByName("zhangfei");
        Assert.assertNotNull(user);
    }

    @Test
    public void selectById() {
        User user101 = userMapper.selectById(101);
        Assert.assertNotNull(user101);
        User user_1 = userMapper.selectById(-1);
        Assert.assertNull(user_1);
    }

    @Test
    public void updatePassword() {
        User user = userMapper.selectByName("test");
        Assert.assertNotNull(user);
        int i = userMapper.updatePassword(user.getId(), "newPassword");
        Assert.assertEquals(i, 1);
        user = userMapper.selectByName("test");
        Assert.assertEquals(user.getPassword(), "newPassword");
    }

    @Test
    public void selectByEmail() {
        User user = userMapper.selectByEmail("nowcoder144@sina.com");
        Assert.assertNotNull(user);
    }

    @Test
    public void updateStatus() {
        User user = userMapper.selectByName("test");
        Assert.assertNotNull(user);
        userMapper.updateStatus(user.getId(), 1);
        user = userMapper.selectById(user.getId());
        Assert.assertEquals(user.getStatus(), 1);
    }

    @Test
    public void updateHeader() {
        User user = userMapper.selectByName("test");
        Assert.assertNotNull(user);
        userMapper.updateHeader(user.getId(), "http://images.nowcoder.com/head/677t.png");
        user = userMapper.selectById(user.getId());
        Assert.assertEquals(user.getHeaderUrl(), "http://images.nowcoder.com/head/677t.png");
    }

}
