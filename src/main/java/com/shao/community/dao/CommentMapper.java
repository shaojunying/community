package com.shao.community.dao;

import com.shao.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author shao
 * @date 2020/9/25 9:16
 */
@Mapper
public interface CommentMapper {
    /**
     * 查找指定帖子或id的评论
     *
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @return
     */
    List<Comment> selectComments(int entityType, int entityId, int offset, int limit);

    /**
     * 查找指定帖子或评论的总评论数
     *
     * @param entityType
     * @param entityId
     * @return
     */
    int selectCommentsRows(int entityType, int entityId);
}
