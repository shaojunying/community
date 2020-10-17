package com.shao.community.service;

import com.shao.community.dao.MessageMapper;
import com.shao.community.entity.Message;
import com.shao.community.util.TrieTreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author shao
 * @date 2020/9/26 16:03
 */
@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private TrieTreeUtil trieTreeUtil;

    public List<Message> selectLatestMessagesWithEveryUser(int userId, int offset, int limit) {
        return messageMapper.selectLatestMessagesWithEveryUser(userId, offset, limit);
    }

    public int selectLatestMessagesRowsWithEveryUser(int userId) {
        return messageMapper.selectLatestMessagesRowsWithEveryUser(userId);
    }

    public int selectUnreadMessagesRows(int userId, String conversationId) {
        return messageMapper.selectUnreadMessagesRows(userId, conversationId);
    }

    public List<Message> selectMessages(String conversationId, int offset, int limit) {
        return messageMapper.selectMessages(conversationId, offset, limit);
    }

    public int selectMessagesRows(String conversationId) {
        return messageMapper.selectMessagesRows(conversationId);
    }

    public int insertMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(trieTreeUtil.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public Message selectById(int id) {
        return messageMapper.selectById(id);
    }

    public int deleteMessage(Message message) {
        return messageMapper.updateStatus(message.getId(), 2);
    }

    public void markMessageRead(Message message) {
        messageMapper.updateStatus(message.getId(), 1);
    }

    public Message selectLatestNotice(int userId, String topic) {
        return messageMapper.selectLatestNotice(userId, topic);
    }

    public int selectNoticesCount(int userId, String topic) {
        return messageMapper.selectNoticesCount(userId, topic);
    }

    public int selectUnreadNoticesCount(int userId, String topic) {
        return messageMapper.selectUnreadNoticesCount(userId, topic);
    }
}
