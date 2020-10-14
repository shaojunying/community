package com.shao.community.service;

import com.shao.community.CommunityApplication;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author shao
 * @date 2020/10/14 16:06
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class HomeServiceTest {

    private final String owner = "fdhufdyufdbdhbcxxcb";
    @Autowired
    private HomeService homeService;

    @Test
    void saveKaptchaToRedis() {
        homeService.saveKaptchaToRedis(owner, "1234");
    }

    @Test
    void getKaptchaFromRedis() {
        String code = homeService.getKaptchaFromRedis(owner);
        System.out.println(code);
        Assert.assertEquals("1234", code);
    }
}
