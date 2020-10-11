package com.shao.community.service;

import com.shao.community.CommunityApplication;
import com.shao.community.util.CommunityConstant;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author shao
 * @date 2020/10/11 19:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class FollowServiceTest {

    @Autowired
    private FollowService followService;

    @Test
    void follow() {
        followService.follow(CommunityConstant.COMMENT_TO_USER, 167, 168);
    }

    @Test
    void unfollow() {
        followService.unfollow(CommunityConstant.COMMENT_TO_USER, 167, 168);
    }
}
