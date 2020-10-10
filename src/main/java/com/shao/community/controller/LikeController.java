package com.shao.community.controller;

import com.shao.community.entity.User;
import com.shao.community.service.LikeService;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shao
 * @date 2020/10/10 20:41
 */
@Controller
@RequestMapping("like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "{entityType}/{entityId}", method = RequestMethod.POST)
    @ResponseBody
    public String like(@PathVariable int entityType, @PathVariable int entityId) {
        User loggedUser = hostHolder.getUser();
        if (loggedUser == null) {
            return CommunityUtil.convertToJson(-1, "请登录后重试!");
        }
        likeService.likeOrUnlike(entityType, entityId, loggedUser.getId());
        Map<String, Object> map = new HashMap<>();
        boolean likeStatus = likeService.getLikeStatus(entityType, entityId, loggedUser.getId());
        map.put("likeStatus", likeStatus);
        Long likesCount = likeService.getLikesCount(entityType, entityId);
        map.put("likeCount", likesCount);
        return CommunityUtil.convertToJson(0, "成功", map);
    }

}
