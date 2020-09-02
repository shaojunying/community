package com.shao.community.dao;

import com.shao.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Author: shao
 * Date: 2020-09-02
 * Time: 17:24
 */
@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    int selectDiscussPostsRows(int userId);
}
