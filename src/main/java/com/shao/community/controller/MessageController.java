package com.shao.community.controller;

import com.shao.community.annotation.LoginRequired;
import com.shao.community.entity.Message;
import com.shao.community.entity.Page;
import com.shao.community.entity.User;
import com.shao.community.service.MessageService;
import com.shao.community.service.UserService;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The type Message controller.
 *
 * @author shao
 * @date 2020 /9/26 16:12
 */
@Controller
@RequestMapping(path = "message")
public class MessageController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    /**
     * Get messages string.
     * model结构
     * map
     * 未读私信总数量
     * 总会话数量
     * list
     * map
     * 未读私信数量
     * 私信总条数
     * message
     * 发信人user
     *
     * @param model the model
     * @param page  the page
     * @return the string
     */
    @RequestMapping(path = "", method = RequestMethod.GET)
    @LoginRequired
    public String getMessages(Model model, Page page) {
        User user = hostHolder.getUser();

        page.setPath("/message");
        // 会话列表中总会话个数
        int messagesRows = messageService.selectLatestMessagesRowsWithEveryUser(user.getId());
        page.setRows(messagesRows);

        Map<String, Object> ans = new HashMap<>();
        // 获取未读私信总条数
        int unreadRows = messageService.selectUnreadMessagesRows(user.getId(), null);
        ans.put("unreadRows", unreadRows);
        // 使用list存储会话列表
        List<Map<String, Object>> conversations = new LinkedList<>();
        List<Message> messages = messageService.selectLatestMessagesWithEveryUser(user.getId(), page.getOffset(), page.getLimit());
        for (Message message : messages) {
            Map<String, Object> map = new HashMap<>();
            // 与当前消息对应用户的未读条数
            int anotherUserId = message.getFromId() == user.getId() ? message.getToId() : message.getFromId();
            String conversationId = user.getId() < anotherUserId ?
                    user.getId() + "_" + anotherUserId : anotherUserId + "_" + user.getId();
            int unreadMessagesRows = messageService.selectUnreadMessagesRows(user.getId(), conversationId);
            map.put("unreadRows", unreadMessagesRows);
            int messagesRows1 = messageService.selectMessagesRows(conversationId);
            map.put("messageRows", messagesRows1);
            map.put("message", message);
            map.put("user", userService.findUserById(anotherUserId));
            conversations.add(map);
        }
        ans.put("conversations", conversations);
        model.addAttribute("map", ans);
        return "site/letter";
    }
}
