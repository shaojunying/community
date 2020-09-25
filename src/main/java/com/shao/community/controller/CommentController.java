package com.shao.community.controller;

import com.shao.community.annotation.LoginRequired;
import com.shao.community.entity.Comment;
import com.shao.community.entity.User;
import com.shao.community.service.CommentService;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author shao
 * @date 2020/9/25 14:41
 */
@Controller
@RequestMapping(path = "comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "", method = RequestMethod.POST)
    @LoginRequired
    public String postComment(String content, Integer entityType, Integer entityId, Integer targetId, Integer postId) {
        User user = hostHolder.getUser();
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUserId(user.getId());
        comment.setStatus(0);
        comment.setEntityId(entityId == null ? 0 : entityId);
        comment.setEntityType(entityType == null ? 0 : entityType);
        comment.setCreateTime(new Date());
        comment.setTargetId(targetId == null ? 0 : targetId);
        commentService.insertComment(comment);
        return String.format("redirect:/discuss-post?id=%d", postId);
    }

}
