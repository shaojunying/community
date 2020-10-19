package com.shao.community.service;

import com.shao.community.CommunityApplication;
import com.shao.community.entity.DiscussPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
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
        SearchHits<DiscussPost> searchHits = elasticsearchService.search("一条测试的帖子", 0, 100);
        System.out.println(searchHits.getTotalHits());
        List<SearchHit<DiscussPost>> searchHitList = searchHits.getSearchHits();
        List<DiscussPost> result = new LinkedList<>();
        for (SearchHit<DiscussPost> discussPostSearchHit : searchHitList) {
            DiscussPost discussPost = discussPostSearchHit.getContent();
            if (!discussPostSearchHit.getHighlightField("title").isEmpty()) {
                discussPost.setTitle(discussPostSearchHit.getHighlightField("title").get(0));
            }
            if (!discussPostSearchHit.getHighlightField("content").isEmpty()) {
                discussPost.setContent(discussPostSearchHit.getHighlightField("content").get(0));
            }
            result.add(discussPost);
        }
        for (DiscussPost discussPost : result) {
            System.out.println(discussPost);
        }
    }
}
