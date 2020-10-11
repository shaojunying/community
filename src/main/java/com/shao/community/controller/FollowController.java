package com.shao.community.controller;

import com.shao.community.entity.User;
import com.shao.community.service.FollowService;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.convertToJson(-1, "请登录后再试");
        }
        followService.follow(entityType, entityId, user.getId());
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
}
