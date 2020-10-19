package com.shao.community.service;

import com.shao.community.CommunityApplication;
import com.shao.community.entity.DiscussPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author shao
 * @date 2020/10/19 20:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchServiceTest {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Test
    public void save() {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setContent("community是一个单词");
        elasticsearchService.save(discussPost);
    }

    @Test
    public void delete() {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setContent("community是一个单词");
        elasticsearchService.delete(discussPost);
    }

    @Test
    public void search() {
        List<DiscussPost> discussPosts = elasticsearchService.search("一条测试的帖子", 0, 100);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
        }
    }
}
