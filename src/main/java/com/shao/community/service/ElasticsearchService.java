package com.shao.community.service;

import com.shao.community.dao.elasticsearch.DiscussPostRepository;
import com.shao.community.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author shao
 * @date 2020/10/19 20:07
 */
@Service
public class ElasticsearchService {

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    /**
     * 存储数据到elasticsearch中
     *
     * @param discussPost the discuss post
     */
    public void save(DiscussPost discussPost) {
        discussPostRepository.save(discussPost);
    }

    /**
     * 从elasticsearch中删除数据
     *
     * @param discussPost the discuss post
     */
    public void delete(DiscussPost discussPost) {
        discussPostRepository.delete(discussPost);
    }

    /**
     * 搜索数据
     *
     * @param text the text
     * @return the list
     */
    public List<DiscussPost> search(String text, int current, int limit) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(text, "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current, limit))
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
        return result;
    }

}
