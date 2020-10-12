package com.shao.community.service;

import com.shao.community.CommunityApplication;
import com.shao.community.util.CommunityConstant;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

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
        followService.follow(CommunityConstant.COMMENT_TO_USER, 111, 167);
    }

    @Test
    void unfollow() {
        followService.unfollow(CommunityConstant.COMMENT_TO_USER, 167, 168);
    }

    @Test
    void isFollowed() {
        boolean followed = followService.isFollowed(CommunityConstant.COMMENT_TO_USER, 111, 167);
        Assert.assertTrue(followed);
    }

    @Test
    void getFolloweeCount() {
        long followeeCount = followService.getFolloweeCount(167, CommunityConstant.COMMENT_TO_USER);
        Assert.assertEquals(1, followeeCount);
    }

    @Test
    void getFollowerCount() {
        long followerCount = followService.getFollowerCount(CommunityConstant.COMMENT_TO_USER, 167);
        Assert.assertEquals(0, followerCount);
    }

    @Test
    void getFollowee() {
        List<Map<String, Object>> followeeList = followService.getFollowee(167, CommunityConstant.COMMENT_TO_USER, 0, 1000);
        Assert.assertNotNull(followeeList);
        System.out.println(followeeList);
    }

    @Test
    void getFollower() {
        List<Map<String, Object>> followerList = followService.getFollower(CommunityConstant.COMMENT_TO_USER, 111, 0, 100);
        Assert.assertNotNull(followerList);
        System.out.println(followerList);
    }
}
