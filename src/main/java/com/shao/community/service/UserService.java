package com.shao.community.service;

import com.shao.community.dao.UserMapper;
import com.shao.community.entity.User;
import com.shao.community.util.CommunityConstant;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Author: shao
 * Date: 2020-09-02
 * Time: 21:35
 */
@Service
public class UserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    public User findUserById(int id) {
        return mapper.selectById(id);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        // 验证用户名、密码、邮箱是否为空
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        // 验证用户名、邮箱是否已存在
        User userSelectedByName = mapper.selectByName(user.getUsername());
        if (userSelectedByName != null) {
            map.put("usernameMsg", "用户名已存在");
            return map;
        }
        User userSelectedByEmail = mapper.selectByEmail(user.getEmail());
        if (userSelectedByEmail != null) {
            map.put("emailMsg", "邮箱已存在");
            return map;
        }

        // 插入数据库
        String salt = CommunityUtil.generateUUID().substring(0, 5);
        String password = CommunityUtil.md5(user.getPassword() + salt);
        user.setSalt(salt);
        user.setPassword(password);
        user.setCreateTime(new Date());
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setStatus(0);
        user.setType(0);
        mapper.insertUser(user);

        // 发送验证邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);

        mailClient.sendMail(user.getEmail(), "验证邮件", content);
        return map;
    }

    public int activate(int userId, String code) {
        User user = mapper.selectById(userId);
        if (user == null) return CommunityConstant.ACTIVATION_FAILURE;
        if (user.getStatus() == 1) {
            return CommunityConstant.ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            mapper.updateStatus(userId, 1);
            return CommunityConstant.ACTIVATION_SUCCESS;
        } else {
            return CommunityConstant.ACTIVATION_FAILURE;
        }
    }
}
