package com.shao.community.service;

import com.shao.community.dao.CommentMapper;
import com.shao.community.dao.DiscussPostMapper;
import com.shao.community.entity.Comment;
import com.shao.community.util.CommunityConstant;
import com.shao.community.util.TrieTreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * @author shao
 * @date 2020/9/25 9:59
 */
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TrieTreeUtil trieTreeUtil;

    public List<Comment> findComments(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectComments(entityType, entityId, offset, limit);
    }

    public int findCommentsRows(int entityType, int entityId) {
        return commentMapper.selectCommentsRows(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertComment(Comment comment) {

        if (comment == null) {
            throw new InvalidParameterException("参数不能为空:" + comment.getClass().getName());
        }

        // 插入评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(trieTreeUtil.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        // 更新评论数量
        if (comment.getEntityType() == CommunityConstant.COMMENT_TO_POST) {
            int counts = commentMapper.selectCommentsRows(comment.getEntityType(), comment.getEntityId());
            discussPostMapper.updateCommentCount(comment.getEntityId(), counts);
        }
        return rows;
    }

    public Comment findCommentById(int commentId) {
        return commentMapper.selectCommentById(commentId);
    }
}
