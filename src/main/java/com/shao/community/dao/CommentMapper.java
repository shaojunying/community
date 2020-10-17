package com.shao.community.dao;

import com.shao.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface Comment mapper.
 *
 * @author shao
 * @date 2020 /9/25 9:16
 */
@Mapper
public interface CommentMapper {
    /**
     * 查找指定帖子或id的评论
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @param offset     the offset
     * @param limit      the limit
     * @return list list
     */
    List<Comment> selectComments(int entityType, int entityId, int offset, int limit);

    /**
     * 查找指定帖子或评论的总评论数
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @return int int
     */
    int selectCommentsRows(int entityType, int entityId);


    /**
     * 插入注释
     *
     * @param comment the comment
     * @return the int
     */
    int insertComment(Comment comment);

    /**
     * Select comment by id comment.
     *
     * @param commentId the comment id
     * @return the comment
     */
    Comment selectCommentById(int commentId);
}
