package com.shao.community.service;

import com.shao.community.dao.DiscussPostMapper;
import com.shao.community.entity.DiscussPost;
import com.shao.community.util.TrieTreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * @author shao
 * @date 2020-09-02 21:33
 */
@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper mapper;

    @Autowired
    private TrieTreeUtil trieTreeUtil;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return mapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostsRows(int userId) {
        return mapper.selectDiscussPostsRows(userId);
    }

    public void addDiscussPost(DiscussPost discussPost) {
        if (discussPost == null) {
            throw new InvalidParameterException("参数不能为空: " + DiscussPost.class.getName());
        }
        // 将html标签进行转义
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        // 过滤敏感词
        discussPost.setTitle(trieTreeUtil.filter(discussPost.getTitle()));
        discussPost.setContent(trieTreeUtil.filter(discussPost.getContent()));
        mapper.insertDiscussPost(discussPost);
    }
}
