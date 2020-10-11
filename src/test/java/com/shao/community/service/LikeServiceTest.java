package com.shao.community.service;

import com.shao.community.CommunityApplication;
import com.shao.community.util.CommunityConstant;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author shao
 * @date 2020/10/10 17:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @Test
    void getLikesCount() {
        Long count = likeService.getLikesCount(CommunityConstant.COMMENT_TO_POST, 275);
        assertEquals(2, count);
    }

    @Test
    void likeOrUnlike() {
        likeService.likeOrUnlike(CommunityConstant.COMMENT_TO_POST, 275, 13, 11);
        boolean likeStatus = likeService.getLikeStatus(CommunityConstant.COMMENT_TO_POST, 275, 13);
        assertTrue(likeStatus);
    }

    @Test
    void getLikeStatus1() {
        boolean likeStatus = likeService.getLikeStatus(CommunityConstant.COMMENT_TO_POST, 275, 167);
        assertTrue(likeStatus);
    }

    @Test
    void getLikeStatus2() {
        boolean likeStatus = likeService.getLikeStatus(CommunityConstant.COMMENT_TO_POST, 275, -1);
        assertFalse(likeStatus);
    }

    @Test
    void getUserLikesCount() {
        int userLikesCount = likeService.getUserLikesCount(11);
        assertEquals(1, userLikesCount);
    }
}
