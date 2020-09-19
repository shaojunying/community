package com.shao.community.dao;

import com.shao.community.CommunityApplication;
import com.shao.community.entity.LoginTicket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Author: shao
 * Date: 2020-09-19
 * Time: 13:40
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
@SpringBootTest
@Transactional
public class LoginTicketMapperTest {

    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Test
    public void insert() {
        LoginTicket loginTicket =
                new LoginTicket(11, "testticket111", 0, new Date());
        int result = loginTicketMapper.insert(loginTicket);
        Assert.assertEquals(result, 1);
    }

    @Test
    public void selectByTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("testticket");
        Assert.assertNotNull(loginTicket);
        Assert.assertEquals(loginTicket.getTicket(), "testticket");
        Assert.assertEquals(loginTicket.getUserId(), 11);
        Assert.assertEquals(loginTicket.getStatus(), 0);
    }

    @Test
    public void updateStatusByTicket() {
        int result = loginTicketMapper.updateStatusByTicket("testticket", 0);
        Assert.assertEquals(result, 1);
        int result1 = loginTicketMapper.updateStatusByTicket("testticket", 1);
        Assert.assertEquals(result1, 1);
    }
}
