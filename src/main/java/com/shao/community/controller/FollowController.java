package com.shao.community.controller;

import com.shao.community.entity.Event;
import com.shao.community.entity.Page;
import com.shao.community.entity.User;
import com.shao.community.event.ProduceEvent;
import com.shao.community.service.FollowService;
import com.shao.community.service.UserService;
import com.shao.community.util.CommunityConstant;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

/**
 * @author shao
 * @date 2020/10/11 19:48
 */
@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private ProduceEvent produceEvent;

    @RequestMapping(value = "follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.convertToJson(-1, "请登录后再试");
        }
        followService.follow(entityType, entityId, user.getId());
        Event event = new Event()
                .setTopic(CommunityConstant.FOLLOW_TOPIC)
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setUserId(user.getId());
        produceEvent.fireEvent(event);
        return CommunityUtil.convertToJson(0, "关注成功");
    }

    @RequestMapping(value = "unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.convertToJson(-1, "请登录后再试");
        }
        followService.unfollow(entityType, entityId, user.getId());
        return CommunityUtil.convertToJson(0, "取消关注成功");
    }

    @RequestMapping(value = "followee/{userId}", method = RequestMethod.GET)
    public String getFollowee(@PathVariable int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new InvalidParameterException("输出的用户id不存在");
        }
        model.addAttribute("user", user);
        page.setRows((int) followService.getFolloweeCount(userId, CommunityConstant.COMMENT_TO_USER));
        page.setPath("followee/" + userId);
        page.setLimit(5);
        List<Map<String, Object>> followee = followService.getFollowee(userId, CommunityConstant.COMMENT_TO_USER, page.getOffset(), page.getLimit());
        // 如果当前用户登录,需要判断其对每个用户的关注状态
        User loggedUser = hostHolder.getUser();
        if (loggedUser != null) {
            for (Map<String, Object> map : followee) {
                User followeeEntity = (User) map.get("user");
                boolean followed = followService.isFollowed(CommunityConstant.COMMENT_TO_USER, followeeEntity.getId(), loggedUser.getId());
                map.put("followed", followed);
            }
        }
        model.addAttribute("followee", followee);
        return "site/followee";
    }

    @RequestMapping(value = "follower/{userId}", method = RequestMethod.GET)
    public String getFollower(@PathVariable int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new InvalidParameterException("输出的用户id不存在");
        }
        model.addAttribute("user", user);
        page.setRows((int) followService.getFollowerCount(CommunityConstant.COMMENT_TO_USER, userId));
        page.setPath("follower/" + userId);
        page.setLimit(5);
        List<Map<String, Object>> follower = followService.getFollower(CommunityConstant.COMMENT_TO_USER, userId, page.getOffset(), page.getLimit());
        // 如果当前用户登录,需要判断其对每个用户的关注状态
        User loggedUser = hostHolder.getUser();
        if (loggedUser != null) {
            for (Map<String, Object> map : follower) {
                User followerEntity = (User) map.get("user");
                boolean followed = followService.isFollowed(CommunityConstant.COMMENT_TO_USER, followerEntity.getId(), loggedUser.getId());
                map.put("followed", followed);
            }
        }
        model.addAttribute("follower", follower);
        return "site/follower";
    }
}
