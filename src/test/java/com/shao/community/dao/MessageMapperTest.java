package com.shao.community.dao;

import com.shao.community.CommunityApplication;
import com.shao.community.entity.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author shao
 * @date 2020/9/26 14:47
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MessageMapperTest {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void selectLatestMessagesWithEveryUser() {
        List<Message> messages = messageMapper.selectLatestMessagesWithEveryUser(111, 0, 10);
        System.out.println(messages);
        Assert.assertEquals(10, messages.size());
    }

    @Test
    public void selectLatestMessagesRowsWithEveryUser() {
        int counts = messageMapper.selectLatestMessagesRowsWithEveryUser(111);
        System.out.println(counts);
        Assert.assertTrue(counts >= 10);
    }

    @Test
    public void selectUnreadMessagesRows() {
        int counts = messageMapper.selectUnreadMessagesRows(111, "111_131");
        System.out.println(counts);
        Assert.assertEquals(2, counts);
    }

    @Test
    public void selectUnreadMessagesRowsWithNullConversation() {
        int counts = messageMapper.selectUnreadMessagesRows(111, null);
        System.out.println(counts);
        Assert.assertTrue(counts > 0);
    }

    @Test
    public void selectMessages() {
        List<Message> messages = messageMapper.selectMessages("111_131", 0, 10);
        for (Message message : messages) {
            System.out.println(message);
        }
        Assert.assertTrue(messages.size() > 0);
    }

    @Test
    public void selectMessagesRows() {
        int counts = messageMapper.selectMessagesRows("111_131");
        System.out.println(counts);
        Assert.assertTrue(counts > 0);
    }
}
