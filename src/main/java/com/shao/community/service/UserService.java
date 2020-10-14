package com.shao.community.service;

import com.shao.community.dao.LoginTicketMapper;
import com.shao.community.dao.UserMapper;
import com.shao.community.entity.LoginTicket;
import com.shao.community.entity.User;
import com.shao.community.util.CommunityConstant;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.MailClient;
import com.shao.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author shao
 * @date 2020-09-02 21:35
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    @Deprecated
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${community.path.domain}")
    private String domain;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>(1);
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
        User userSelectedByName = userMapper.selectByName(user.getUsername());
        if (userSelectedByName != null) {
            map.put("usernameMsg", "用户名已存在");
            return map;
        }
        User userSelectedByEmail = userMapper.selectByEmail(user.getEmail());
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
        userMapper.insertUser(user);

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
        User user = userMapper.selectById(userId);
        if (user == null) {
            return CommunityConstant.ACTIVATION_FAILURE;
        }
        if (user.getStatus() == 1) {
            return CommunityConstant.ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return CommunityConstant.ACTIVATION_SUCCESS;
        } else {
            return CommunityConstant.ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSecond) {
        Map<String, Object> map = new HashMap<>(1);
        // 判断用户名密码是否为空
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        // 判断用户名、账号状态、密码是否正确
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "用户名不存在");
            return map;
        }
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活");
            return map;
        }
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码错误，请重新输入");
            return map;
        }
        String ticket = CommunityUtil.generateUUID();
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        redisTemplate.opsForValue().set(ticketKey, user.getId(), expiredSecond, TimeUnit.SECONDS);
        map.put("ticket", ticket);
        return map;
    }

    public void logout(String ticket) {
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        redisTemplate.delete(ticketKey);
    }

    @Deprecated
    public LoginTicket getLoginTicketByTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeaderUrl(int id, String headerUrl) {
        return userMapper.updateHeader(id, headerUrl);
    }

    public int updatePassword(int id, String newPassword) {
        return userMapper.updatePassword(id, newPassword);
    }

    public User selectByName(String username) {
        return userMapper.selectByName(username);
    }

    public User getUserByTicket(String ticket) {
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        Integer userId = (Integer) redisTemplate.opsForValue().get(ticketKey);
        if (userId == null) {
            return null;
        }
        User user = findUserById(userId);
        return user;
    }
}
