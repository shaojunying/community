package com.shao.community.controller;

import com.shao.community.entity.DiscussPost;
import com.shao.community.entity.Page;
import com.shao.community.entity.User;
import com.shao.community.service.ElasticsearchService;
import com.shao.community.service.LikeService;
import com.shao.community.service.UserService;
import com.shao.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author shao
 * @date 2020/10/19 20:53
 */
@Controller
@RequestMapping("search")
public class SearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model) {
        SearchHits<DiscussPost> searchHits = elasticsearchService.search(keyword, page.getCurrent() - 1, page.getLimit());
        System.out.println(page.getOffset());
        System.out.println(page.getLimit());
        System.out.println(searchHits);
        List<Map<String, Object>> ans = new LinkedList<>();

        List<DiscussPost> discussPosts = getDiscussPostsFromSearchHist(searchHits);
        for (DiscussPost discussPost : discussPosts) {
            Map<String, Object> map = new HashMap<>();
            map.put("discussPost", discussPost);
            int userId = discussPost.getUserId();
            User user = userService.findUserById(userId);
            map.put("user", user);
            Long likes = likeService.getLikesCount(CommunityConstant.COMMENT_TO_POST, discussPost.getId());
            map.put("likes", likes);
            ans.add(map);
        }

        System.out.println(searchHits.getTotalHits());
        page.setRows((int) searchHits.getTotalHits());
        page.setPath("/search?keyword=" + keyword);

        model.addAttribute("ans", ans);
        model.addAttribute("keyword", keyword);
        return "/site/search";
    }

    private List<DiscussPost> getDiscussPostsFromSearchHist(SearchHits<DiscussPost> searchHits) {
        List<SearchHit<DiscussPost>> searchHitList = searchHits.getSearchHits();
        List<DiscussPost> discussPosts = new LinkedList<>();
        for (SearchHit<DiscussPost> discussPostSearchHit : searchHitList) {
            DiscussPost discussPost = discussPostSearchHit.getContent();
            if (!discussPostSearchHit.getHighlightField("title").isEmpty()) {
                discussPost.setTitle(discussPostSearchHit.getHighlightField("title").get(0));
            }
            if (!discussPostSearchHit.getHighlightField("content").isEmpty()) {
                discussPost.setContent(discussPostSearchHit.getHighlightField("content").get(0));
            }
            discussPosts.add(discussPost);
        }
        return discussPosts;
    }

}
