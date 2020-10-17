package com.shao.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.shao.community.annotation.LoginRequired;
import com.shao.community.entity.Message;
import com.shao.community.entity.Page;
import com.shao.community.entity.User;
import com.shao.community.service.MessageService;
import com.shao.community.service.UserService;
import com.shao.community.util.CommunityConstant;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author shao
 * @date 2020/10/16 21:39
 */
@Controller
@RequestMapping(path = "notice")
public class NoticeController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @LoginRequired
    public String getNotice(Model model) {
        User loggedUser = hostHolder.getUser();

        int unreadMessagesRows = messageService.selectUnreadMessagesRows(loggedUser.getId(), null);
        model.addAttribute("unreadMessagesCount", unreadMessagesRows);

        int allUnreadNoticesCount = messageService.selectUnreadNoticesCount(loggedUser.getId(), null);
        model.addAttribute("unreadNoticesCount", allUnreadNoticesCount);

        // 评论类通知
        Map<String, Object> commentNoticeMap = getNoticeInfo(loggedUser, CommunityConstant.COMMENT_TOPIC);
        model.addAttribute("commentMap", commentNoticeMap);

        // 点赞类通知
        Map<String, Object> likeNoticeMap = getNoticeInfo(loggedUser, CommunityConstant.LIKE_TOPIC);
        model.addAttribute("likeMap", likeNoticeMap);

        // 关注类通知
        Map<String, Object> followNoticeMap = getNoticeInfo(loggedUser, CommunityConstant.FOLLOW_TOPIC);
        model.addAttribute("followMap", followNoticeMap);

        return "/site/notice";
    }

    /**
     * 获取指定topic对应的最新通知,总通知数,未读通知数
     *
     * @param loggedUser 用户
     * @param topic
     * @return
     */
    private Map<String, Object> getNoticeInfo(User loggedUser, String topic) {
        Message latestNotice = messageService.selectLatestNotice(loggedUser.getId(), topic);
        if (latestNotice == null) {
            return null;
        }
        // 存储该类型通知的所有信息
        Map<String, Object> map = new HashMap<>();
        // 存储该类型最新通知的信息
        Map<String, Object> latestNoticeMap = new HashMap<>();

        // 从content中获取topic的信息
        String content = latestNotice.getContent();
        content = HtmlUtils.htmlUnescape(content);
        Map<String, Object> contentMap = JSONObject.parseObject(content, HashMap.class);

        int fromId = (int) contentMap.get("userId");
        User fromUser = userService.findUserById(fromId);
        latestNoticeMap.put("notice", latestNotice);
        latestNoticeMap.put("fromUser", fromUser);
        latestNoticeMap.put("entityType", contentMap.get("entityType"));
        latestNoticeMap.put("entityId", contentMap.get("entityId"));
        if (topic.equals(CommunityConstant.COMMENT_TOPIC) || topic.equals(CommunityConstant.LIKE_TOPIC)) {
            latestNoticeMap.put("postId", contentMap.get("postId"));
        }
        map.put("latestMap", latestNoticeMap);

        int noticesCount = messageService.selectNoticesCount(loggedUser.getId(), topic);
        map.put("count", noticesCount);
        int unreadNoticesCount = messageService.selectUnreadNoticesCount(loggedUser.getId(), topic);
        map.put("unreadCount", unreadNoticesCount);
        return map;
    }

    @RequestMapping(value = "{topic}", method = RequestMethod.GET)
    @LoginRequired
    public String getNoticeDetail(@PathVariable String topic, Page page, Model model) {
        User loggedUser = hostHolder.getUser();
        if (!topic.equals(CommunityConstant.COMMENT_TOPIC) && !topic.equals(CommunityConstant.LIKE_TOPIC) && !topic.equals(CommunityConstant.FOLLOW_TOPIC)) {
            throw new InvalidParameterException("topic参数不合法: " + topic);
        }
        int noticesCount = messageService.selectNoticesCount(loggedUser.getId(), topic);
        page.setRows(noticesCount);
        page.setLimit(5);
        page.setPath("/notice/" + topic);

        List<Message> messageList = messageService.selectNotices(loggedUser.getId(), topic, page.getOffset(), page.getLimit());

        // 将所有消息标记为已读
        for (Message message : messageList) {
            messageService.markMessageRead(message);
        }

        List<Map<String, Object>> ans = new LinkedList<>();
        for (Message message : messageList) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", message);
            String content = message.getContent();
            if (content == null) {
                throw new RuntimeException("解析消息内容出错");
            }
            content = HtmlUtils.htmlUnescape(content);
            Map<String, Object> contentMap = JSONObject.parseObject(content, HashMap.class);
            if (contentMap == null) {
                throw new RuntimeException("解析消息内容出错");
            }
            map.put("entityType", contentMap.get("entityType"));
            map.put("entityId", contentMap.get("entityId"));
            int userId = (int) contentMap.get("userId");
            map.put("entityUser", userService.findUserById(userId));
            if (topic.equals(CommunityConstant.LIKE_TOPIC) || topic.equals(CommunityConstant.COMMENT_TOPIC)) {
                map.put("postId", contentMap.get("postId"));
            }
            User fromUser = userService.findUserById(message.getFromId());
            map.put("fromUser", fromUser);
            ans.add(map);
        }
        model.addAttribute("ans", ans);
        return "/site/notice-detail";
    }

}
