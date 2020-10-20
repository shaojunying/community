package com.shao.community.controller;

import com.shao.community.annotation.LoginRequired;
import com.shao.community.entity.Comment;
import com.shao.community.entity.DiscussPost;
import com.shao.community.entity.Event;
import com.shao.community.entity.User;
import com.shao.community.event.ProduceEvent;
import com.shao.community.service.CommentService;
import com.shao.community.service.DiscussPostService;
import com.shao.community.util.CommunityConstant;
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
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private ProduceEvent produceEvent;

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

        // 创建评论事件
        Event event = new Event()
                .setTopic(CommunityConstant.COMMENT_TOPIC)
                .setEntityId(comment.getEntityId())
                .setEntityType(comment.getEntityType())
                .setUserId(user.getId())
                .setData("postId", postId);
        if (entityType == CommunityConstant.COMMENT_TO_POST) {
            DiscussPost discussPost = discussPostService.findDiscussPost(event.getEntityId());
            if (discussPost == null) {
                throw new RuntimeException("帖子id不正确");
            }
            event.setEntityUserId(discussPost.getUserId());
        } else {
            Comment targetComment = commentService.findCommentById(event.getEntityId());
            if (targetComment == null) {
                throw new RuntimeException("评论id不正确");
            }
            event.setEntityUserId(event.getUserId());
        }
        produceEvent.fireEvent(event);
        if (entityType == CommunityConstant.COMMENT_TO_POST) {
            Event publishEvent = new Event()
                    .setTopic(CommunityConstant.PUBLISH_TOPIC)
                    .setEntityId(comment.getEntityId())
                    .setEntityType(CommunityConstant.COMMENT_TO_POST)
                    .setUserId(user.getId());
            produceEvent.fireEvent(publishEvent);
        }
        return String.format("redirect:/discuss-post?id=%d", postId);
    }

}
