package com.shao.community.controller;

import com.shao.community.entity.DiscussPost;
import com.shao.community.entity.Page;
import com.shao.community.entity.User;
import com.shao.community.service.DiscussPostService;
import com.shao.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Author: shao
 * Date: 2020-09-02
 * Time: 21:43
 */
@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page) {
        page.setRows(discussPostService.findDiscussPostsRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, 0, 10);
        List<Map<String, Object>> discussPosts = new LinkedList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("page", page);
        return "/index";

    }

    @RequestMapping(path = "register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(path = "register", method = RequestMethod.POST)
    public String postRegisterPage(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.size() == 0) {
            // 注册成功
            Context context = new Context();
            context.setVariable("text", "注册成功,我们向您发送了激活邮件,请尽快激活.");
            context.setVariable("target", "/index");
            return "/site/operate-result";
        } else {
            // 注册失败 在注册页显示错误信息
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

}
