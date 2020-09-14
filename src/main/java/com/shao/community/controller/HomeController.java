package com.shao.community.controller;

import com.google.code.kaptcha.Producer;
import com.shao.community.entity.DiscussPost;
import com.shao.community.entity.Page;
import com.shao.community.entity.User;
import com.shao.community.service.DiscussPostService;
import com.shao.community.service.UserService;
import com.shao.community.util.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
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

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private Producer producer;

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

    @RequestMapping(path = "login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    @RequestMapping(path = "register", method = RequestMethod.POST)
    public String postRegisterPage(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.size() == 0) {
            // 注册成功
            model.addAttribute("text", "注册成功,我们向您发送了激活邮件,请尽快激活.");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            // 注册失败 在注册页显示错误信息
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{userId}/{activationCode}", method = RequestMethod.GET)
    public String activate(@PathVariable int userId, @PathVariable String activationCode, Model model) {
        int result = userService.activate(userId, activationCode);
        switch (result) {
            case CommunityConstant.ACTIVATION_SUCCESS:
                // 注册成功
                model.addAttribute("text", "激活成功,请登录!");
                model.addAttribute("target", "/login");
                break;
            case CommunityConstant.ACTIVATION_REPEAT:
                // 重复激活
                model.addAttribute("text", "账号已激活,请登录!");
                model.addAttribute("target", "/login");
                break;
            case CommunityConstant.ACTIVATION_FAILURE:
                // 激活失败
                model.addAttribute("text", "激活失败,请重试!");
                model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);

        // 将验证码文字保存进session
        session.setAttribute("kaptcha", text);

        // 将图片发送回浏览器
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "jpg", outputStream);
        } catch (IOException e) {
            logger.error("相应验证码失败:" + e.getMessage());
        }
    }

}
