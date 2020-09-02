package com.shao.community.service;

import com.shao.community.dao.DiscussPostMapper;
import com.shao.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: shao
 * Date: 2020-09-02
 * Time: 21:33
 */
@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper mapper;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return mapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostsRows(int userId) {
        return mapper.selectDiscussPostsRows(userId);
    }

}
