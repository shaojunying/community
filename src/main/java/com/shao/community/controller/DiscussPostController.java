package com.shao.community.controller;

import com.shao.community.entity.*;
import com.shao.community.event.ProduceEvent;
import com.shao.community.service.CommentService;
import com.shao.community.service.DiscussPostService;
import com.shao.community.service.LikeService;
import com.shao.community.service.UserService;
import com.shao.community.util.CommunityConstant;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author shao
 * @date 2020/9/22 14:55
 */
@Controller
@RequestMapping(path = "discuss-post")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ProduceEvent produceEvent;

    @RequestMapping(path = "", method = RequestMethod.POST)
    @ResponseBody
    public String postDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            // 没有登录
            return CommunityUtil.convertToJson(-1, "请登录之后再尝试发帖");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPost.setUserId(user.getId());
        discussPostService.addDiscussPost(discussPost);

        // 向elasticsearch中保存帖子
        Event event = new Event()
                .setTopic(CommunityConstant.PUBLISH_TOPIC)
                .setUserId(user.getId())
                .setEntityType(CommunityConstant.COMMENT_TO_POST)
                .setEntityId(discussPost.getId());
        produceEvent.fireEvent(event);

        return CommunityUtil.convertToJson(0, "成功");
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String getDiscussPost(@RequestParam("id") int id, Model model, Page page) {
        DiscussPost discussPost = discussPostService.findDiscussPost(id);
        if (discussPost == null) {
            model.addAttribute("text", "要查询的帖子不存在,即将跳转到首页");
            model.addAttribute("target", "/index");
            return "site/operate-result";
        }
        model.addAttribute(discussPost);
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute(user);

        // 当前用户是否点赞该帖子
        User loggedUser = hostHolder.getUser();
        boolean postLiked = loggedUser != null && likeService.getLikeStatus(CommunityConstant.COMMENT_TO_POST, discussPost.getId(), loggedUser.getId());
        model.addAttribute("postLikeStatus", postLiked);

        // 查询帖子的点赞数
        long postLikesCount = likeService.getLikesCount(CommunityConstant.COMMENT_TO_POST, discussPost.getId());
        model.addAttribute("postLikesCount", postLikesCount);

        // 查询帖子的评论
        page.setPath(String.format("/discuss-post?id=%d", id));
        page.setRows(discussPost.getCommentCount());
        page.setLimit(5);
        List<Comment> comments = commentService.findComments(CommunityConstant.COMMENT_TO_POST,
                discussPost.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentsList = new LinkedList<>();
        for (Comment comment : comments) {
            Map<String, Object> map = new HashMap<>();
            // 保存评论的用户信息,以及评论相关信息
            User publisher = userService.findUserById(comment.getUserId());
            map.put("publisher", publisher);
            map.put("comment", comment);

            // 保存评论的点赞数信息
            Long commentLikesCount = likeService.getLikesCount(CommunityConstant.COMMENT_TO_COMMENT, comment.getId());
            map.put("commentLikesCount", commentLikesCount);

            // 保存评论的点赞状态
            boolean commentLikeStatus = loggedUser != null && likeService.getLikeStatus(CommunityConstant.COMMENT_TO_COMMENT, comment.getId(), loggedUser.getId());
            map.put("commentLikeStatus", commentLikeStatus);


            // 保存子评论的信息
            List<Comment> subComments = commentService.findComments(CommunityConstant.COMMENT_TO_COMMENT,
                    comment.getId(), 0, Integer.MAX_VALUE);
            List<Map<String, Object>> subCommentsList = new LinkedList<>();
            if (subComments != null) {
                for (Comment subComment : subComments) {
                    Map<String, Object> subCommentMap = new HashMap<>();
                    // 回复原评论的 ==> 保存评论信息,发布者信息
                    // 回复子评论的 ==> 保存评论信息,发布者信息,接收者信息
                    subCommentMap.put("comment", subComment);

                    // 子评论的点赞数信息
                    long subCommentLikesCount = likeService.getLikesCount(CommunityConstant.COMMENT_TO_COMMENT, subComment.getId());
                    subCommentMap.put("subCommentLikesCount", subCommentLikesCount);

                    // 子评论的点赞状态
                    boolean subCommentLikeStatus = loggedUser != null && likeService.getLikeStatus(CommunityConstant.COMMENT_TO_COMMENT, subComment.getId(), loggedUser.getId());
                    subCommentMap.put("subCommentLikeStatus", subCommentLikeStatus);

                    User subCommentPublisher = userService.findUserById(subComment.getUserId());
                    subCommentMap.put("publisher", subCommentPublisher);
                    subCommentMap.put("receiver", null);
                    if (subComment.getTargetId() != 0) {
                        // 回复其他子评论的
                        User subCommentReceiver = userService.findUserById(subComment.getTargetId());
                        subCommentMap.put("receiver", subCommentReceiver);
                    }
                    subCommentsList.add(subCommentMap);
                }
            }
            map.put("subComments", subCommentsList);
            // 记录某评论的子评论总条数
            int subCommentsRows = commentService.findCommentsRows(CommunityConstant.COMMENT_TO_COMMENT, comment.getId());
            map.put("subCommentsRows", subCommentsRows);
            commentsList.add(map);
        }
        model.addAttribute("commentsList", commentsList);
        return "site/discuss-detail";
    }
}
