package com.shao.community.dao;

import com.shao.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface Discuss post mapper.
 *
 * @author shao
 * @date 2020 -09-02 17:24
 */
@Mapper
public interface DiscussPostMapper {


    /**
     * 查询指定用户的全部帖子,首先按照"置顶","普通"的顺序排序,之后再按照帖子创建的时间从近到远排序.
     *
     * @param userId 要查询的用户id,id为0时表示查询所有用户的帖子
     * @param offset 从第offset个帖子开始查询,之前的帖子跳过
     * @param limit  查询的最大数量
     * @return 查询到的帖子列表 list
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * 获取指定用户总帖子数
     *
     * @param userId 要查询的用户id,为0时表示所有用户
     * @return 指定用户的总帖子数 int
     */
    int selectDiscussPostsRows(int userId);


    /**
     * 插入一条帖子
     *
     * @param discussPost the discuss post
     * @return int
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 根据帖子id查询帖子
     *
     * @param id the id
     * @return discuss post
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * 更新指定帖子的总评论数量
     *
     * @param id           the id
     * @param commentCount the comment count
     * @return the int
     */
    int updateCommentCount(int id, int commentCount);
}
