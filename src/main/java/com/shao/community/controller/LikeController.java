package com.shao.community.controller;

import com.shao.community.entity.User;
import com.shao.community.service.LikeService;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    /**
     * Like string.
     *
     * @param entityType   被点赞实体类型
     * @param entityId     被点赞实体id
     * @param entityUserId 被点赞用户id
     * @return the string
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId) {
        User loggedUser = hostHolder.getUser();
        if (loggedUser == null) {
            return CommunityUtil.convertToJson(-1, "请登录后重试!");
        }
        likeService.likeOrUnlike(entityType, entityId, loggedUser.getId(), entityUserId);
        Map<String, Object> map = new HashMap<>();
        boolean likeStatus = likeService.getLikeStatus(entityType, entityId, loggedUser.getId());
        map.put("likeStatus", likeStatus);
        Long likesCount = likeService.getLikesCount(entityType, entityId);
        map.put("likeCount", likesCount);
        return CommunityUtil.convertToJson(0, "成功", map);
    }
}
