package com.shao.community.controller;

import com.shao.community.annotation.LoginRequired;
import com.shao.community.entity.User;
import com.shao.community.service.FollowService;
import com.shao.community.service.LikeService;
import com.shao.community.service.UserService;
import com.shao.community.util.CommunityConstant;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidParameterException;

/**
 * @author shao
 * @date 2020-09-20 17:09
 */
@Controller
@RequestMapping("user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domainPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @LoginRequired
    @RequestMapping(path = "setting", method = RequestMethod.GET)
    public String updateUserInfo() {
        return "site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "upload", method = RequestMethod.POST)
    public String uploadImage(MultipartFile uploadImage, Model model) throws IOException {
        User user = hostHolder.getUser();
        // 上传图片
        if (uploadImage == null) {
            model.addAttribute("error", "您还没有选择图片");
            return "site/setting";
        }
        // 提取后缀名
        String filename = uploadImage.getOriginalFilename();
        if (filename == null) {
            model.addAttribute("error", "图片格式不正确,请重新选择");
            return "site/setting";
        }
        int index = filename.lastIndexOf(".");
        if (index == -1) {
            model.addAttribute("error", "图片格式不正确,请重新选择");
            return "site/setting";
        }
        String suffix = filename.substring(index);
        filename = CommunityUtil.generateUUID() + suffix;
        // 将文件保存进服务器
        File file = new File(uploadPath + "/" + filename);
        try {
            uploadImage.transferTo(file);
        } catch (IOException e) {
            logger.error("上传图片出错", e);
            throw e;
        }
        // 更新用户头像路径
        String headerUrl = domainPath + "user/header/" + filename;
        userService.updateHeaderUrl(user.getId(), headerUrl);
        return "redirect:/index";
    }

    @RequestMapping(path = "header/{filename}", method = RequestMethod.GET)
    public void getHeaderImage(@PathVariable String filename, HttpServletResponse httpServletResponse) {
        // 服务器存放路径
        filename = uploadPath + "/" + filename;
        // 文件后缀
        int index = filename.lastIndexOf(".");
        if (index == -1) {
            throw new RuntimeException("读取文件时文件路径错误");
        }
        String suffix = filename.substring(index);
        // 响应图片
        httpServletResponse.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(filename);
                OutputStream os = httpServletResponse.getOutputStream()
        ) {
            byte[] buffer = new byte[1024];
            int b;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }

        } catch (IOException e) {
            logger.error("读取文件错误: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @LoginRequired
    @RequestMapping(path = "changePassword", method = RequestMethod.POST)
    public String changePassword(String oldPassword, String newPassword, Model model) {

        User user = hostHolder.getUser();

        // 旧密码不能为空
        if (StringUtils.isBlank(oldPassword)) {
            model.addAttribute("oldPasswordMsg", "旧密码不能为空");
            return "site/setting";
        }

        // 新密码不能为空
        if (StringUtils.isBlank(newPassword)) {
            model.addAttribute("newPasswordMsg", "新密码不能为空");
            return "site/setting";
        }

        // 旧密码不正确
        oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
        if (!user.getPassword().equals(oldPassword)) {
            model.addAttribute("oldPasswordMsg", "旧密码不正确");
            return "site/setting";
        }
        // 修改密码
        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        userService.updatePassword(user.getId(), newPassword);
        model.addAttribute("text", "修改密码成功,即将跳转到首页");
        model.addAttribute("target", "index");
        return "site/operate-result";
    }

    @RequestMapping(value = "profile/{userId}", method = RequestMethod.GET)
    public String getProfile(@PathVariable int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            // id不存在
            throw new InvalidParameterException("输入的用户id不存在");
        }
        model.addAttribute("user", user);
        int userLikesCount = likeService.getUserLikesCount(userId);
        model.addAttribute("userLikesCount", userLikesCount);
        // 关注数
        long followeeCount = followService.getFolloweeCount(userId, CommunityConstant.COMMENT_TO_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数
        long followerCount = followService.getFollowerCount(CommunityConstant.COMMENT_TO_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 关注状态
        User loggedUser = hostHolder.getUser();
        if (loggedUser != null) {
            boolean followed = followService.isFollowed(CommunityConstant.COMMENT_TO_USER, userId, loggedUser.getId());
            model.addAttribute("followed", followed);
        }
        return "site/profile";
    }

}
