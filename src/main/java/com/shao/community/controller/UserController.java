package com.shao.community.controller;

import com.shao.community.entity.User;
import com.shao.community.service.UserService;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.HostHolder;
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

/**
 * Author: shao
 * Date: 2020-09-20
 * Time: 17:09
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


    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String updateUserInfo() {
        return "site/setting";
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadImage(MultipartFile uploadImage, Model model) throws IOException {
        User user = hostHolder.getUser();
        if (user == null) {
            // 获取不到用户信息
            model.addAttribute("text", "获取登录信息失败,请重新登陆");
            model.addAttribute("target", "/login");
            return "site/operate-result";
        }
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
        String headerUrl = domainPath + "/user/header/" + filename;
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
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }

        } catch (IOException e) {
            logger.error("读取文件错误: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
