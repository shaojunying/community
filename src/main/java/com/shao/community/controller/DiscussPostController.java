package com.shao.community.controller;

import com.shao.community.entity.DiscussPost;
import com.shao.community.entity.User;
import com.shao.community.service.DiscussPostService;
import com.shao.community.service.UserService;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private UserService userService;

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

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String getDiscussPost(@RequestParam("id") int id, Model model) {
        DiscussPost discussPost = discussPostService.findDiscussPost(id);
        if (discussPost == null) {
            model.addAttribute("text", "要查询的帖子不存在,即将跳转到首页");
            model.addAttribute("target", "/index");
            return "site/operate-result";
        }
        model.addAttribute(discussPost);
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute(user);
        return "site/discuss-detail";
    }
}
