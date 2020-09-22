package com.shao.community.controller;

import com.shao.community.entity.DiscussPost;
import com.shao.community.entity.User;
import com.shao.community.service.DiscussPostService;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

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
        return CommunityUtil.convertToJson(0, "成功");
    }
}
