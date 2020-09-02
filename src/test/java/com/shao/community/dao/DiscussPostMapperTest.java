package com.shao.community.dao;

import com.shao.community.CommunityApplication;
import com.shao.community.entity.DiscussPost;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Author: shao
 * Date: 2020-09-02
 * Time: 17:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class DiscussPostMapperTest {
    @Autowired
    private DiscussPostMapper mapper;

    @Test
    public void selectDiscussPosts() {
        List<DiscussPost> discussPosts = mapper.selectDiscussPosts(0, 0, 10);
        for (DiscussPost post : discussPosts) {
            System.out.println(post);
        }
        Assert.assertEquals(discussPosts.size(), 10);
    }

    @Test
    public void selectDiscussPostsRows() {
        int rows = mapper.selectDiscussPostsRows(0);
        System.out.println(rows);
        Assert.assertTrue(rows > 0);
    }
}
