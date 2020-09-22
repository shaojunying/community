package com.shao.community.service;

import com.shao.community.CommunityApplication;
import com.shao.community.entity.DiscussPost;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author shao
 * @date 2020/9/22 14:42
 */
@ContextConfiguration(classes = CommunityApplication.class)
@SpringBootTest
@RunWith(SpringRunner.class)
public class DiscussPostServiceTest {
    @Autowired
    DiscussPostService discussPostService;

    @Test
    public void addDiscussPost() {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setTitle("这是一条测试帖子,不赌博");
        discussPost.setContent("这是帖子的内容<script>alert('111')</script>");
        discussPost.setUserId(167);
        discussPostService.addDiscussPost(discussPost);
        Assert.assertEquals("这是一条测试帖子,不***", discussPost.getTitle());
        Assert.assertNotEquals("这是帖子的内容<script>alert('111')</script>", discussPost.getContent());
        System.out.println(discussPost);
    }
}
