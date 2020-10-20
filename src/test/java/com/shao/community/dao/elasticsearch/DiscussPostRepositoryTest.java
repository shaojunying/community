package com.shao.community.dao.elasticsearch;

import com.shao.community.CommunityApplication;
import com.shao.community.dao.DiscussPostMapper;
import com.shao.community.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

/**
 * @author shao
 * @date 2020/10/18 17:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class DiscussPostRepositoryTest {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Test
    public void testInsert() {
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(114));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(234));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(235));
    }

    // WARNING: 会删除全部数据 不要随便执行!!
//    @Test
//    public void testInsertAll() {
//        discussPostRepository.deleteAll();
//        for (int id = 0; id < 400; id++) {
//            DiscussPost discussPost = discussPostMapper.selectDiscussPostById(id);
//            if (discussPost != null) {
//                discussPostRepository.save(discussPost);
//            }
//        }
//    }

    @Test
    public void testUpdate() {
        DiscussPost discussPost = discussPostMapper.selectDiscussPostById(114);
        Assert.assertNotNull(discussPost);
        discussPost.setContent("哈哈啊");
        discussPostRepository.save(discussPost);
    }

    @Test
    public void testDelete() {
        discussPostRepository.delete(discussPostMapper.selectDiscussPostById(114));
    }

//    @Test
//    public void testDeleteAll() {
//        discussPostRepository.deleteAll();
//    }

    @Test
    public void testSearch() {
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        Page<DiscussPost> page = discussPostRepository.search(build);
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());
        for (DiscussPost discussPost : page) {
            System.out.println(discussPost);
        }
    }

    @Test
    public void testSearchWithHighlight() {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title"), new HighlightBuilder.Field("content")
                ).build();
        SearchHits<DiscussPost> searchHits = elasticsearchOperations.search(searchQuery, DiscussPost.class);
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
        System.out.println(result);
    }
}
