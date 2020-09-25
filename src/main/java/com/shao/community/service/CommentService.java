package com.shao.community.service;

import com.shao.community.dao.CommentMapper;
import com.shao.community.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shao
 * @date 2020/9/25 9:59
 */
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> findComments(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectComments(entityType, entityId, offset, limit);
    }

    public int findCommentsRows(int entityType, int entityId) {
        return commentMapper.selectCommentsRows(entityType, entityId);
    }
}
