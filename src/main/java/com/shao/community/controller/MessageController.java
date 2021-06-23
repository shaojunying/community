package com.shao.community.controller;

import com.shao.community.annotation.LoginRequired;
import com.shao.community.entity.Message;
import com.shao.community.entity.Page;
import com.shao.community.entity.User;
import com.shao.community.service.MessageService;
import com.shao.community.service.UserService;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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

        int unreadNoticesCount = messageService.selectUnreadNoticesCount(user.getId(), null);
        ans.put("unreadNoticesCount", unreadNoticesCount);
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
            // 这里可能查询不到指定的用户
            User anotherUser = userService.findUserById(anotherUserId);
            if (anotherUser == null){
                // 查询不到该用户
                continue;
            }
            map.put("user", userService.findUserById(anotherUserId));
            conversations.add(map);
        }
        ans.put("conversations", conversations);
        model.addAttribute("map", ans);
        return "site/letter";
    }


    @RequestMapping(path = "detail/{conversationId}", method = RequestMethod.GET)
    @LoginRequired
    public String getMessages(Model model, Page page, @PathVariable String conversationId) {
        User user = hostHolder.getUser();
        String[] strings = conversationId.split("_");
        if (strings.length != 2 || (Integer.parseInt(strings[0]) != user.getId() && Integer.parseInt(strings[1]) != user.getId())) {
            model.addAttribute("text", "访问非法,将跳转到首页");
            model.addAttribute("target", "index");
            return "site/operate-result";
        }
        int user1 = Integer.parseInt(strings[0]);
        int user2 = Integer.parseInt(strings[1]);
        page.setPath(String.format("/message/detail/%s", conversationId));
        int rows = messageService.selectMessagesRows(conversationId);
        page.setRows(rows);
        int anotherUserId = user1 == user.getId() ? user2 : user1;
        model.addAttribute("anotherUserName", userService.findUserById(anotherUserId).getUsername());
        List<Message> messageList = messageService.selectMessages(conversationId, page.getOffset(), page.getLimit());

        // 将消息标记为已读
        for (Message message : messageList) {
            if (message.getToId() == user.getId()) {
                messageService.markMessageRead(message);
            }
        }

        List<Map<String, Object>> list = new LinkedList<>();
        for (Message message : messageList) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", message);
            map.put("user", userService.findUserById(message.getFromId()));
            list.add(map);
        }
        model.addAttribute("list", list);
        return "site/letter-detail";
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public String postMessage(String toName, String content) {
        User user = hostHolder.getUser();
        User toUser = userService.selectByName(toName);
        if (toUser == null) {
            return CommunityUtil.convertToJson(-1, "输入的用户名不正确");
        }
        Message message = new Message();
        message.setContent(content);
        message.setFromId(user.getId());

        message.setToId(toUser.getId());
        String conversationId = message.getFromId() > message.getToId() ?
                message.getToId() + "_" + message.getFromId() : message.getFromId() + "_" + message.getToId();
        message.setConversationId(conversationId);
        message.setStatus(0);
        message.setCreateTime(new Date());
        messageService.insertMessage(message);
        return CommunityUtil.convertToJson(0, "发送成功");
    }

    @RequestMapping(path = "", method = RequestMethod.DELETE)
    @LoginRequired
    @ResponseBody
    public String postMessage(int id) {
        System.out.println(111);
        User user = hostHolder.getUser();
        Message message = messageService.selectById(id);
        if (message == null || (user.getId() != message.getFromId() && user.getId() != message.getToId())) {
            return CommunityUtil.convertToJson(-1, "输入的id有误");
        }

        // 更新状态
        messageService.deleteMessage(message);
        return CommunityUtil.convertToJson(0, "删除成功");
    }
}
