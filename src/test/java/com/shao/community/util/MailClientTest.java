package com.shao.community.util;

import com.shao.community.CommunityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Author: shao
 * Date: 2020-09-05
 * Time: 16:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailClientTest {
    @Autowired
    MailClient mailClient;

    @Autowired
    TemplateEngine engine;

    @Test
    public void sendTextMail() {
        mailClient.sendMail("shaojunying1@gmail.com", "Test", "这是一封测试邮件");
    }

    @Test
    public void sendHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "邵俊颖");

        String content = engine.process("/mail/test", context);
        System.out.println(content);

        mailClient.sendMail("shaojunying1@gmail.com", "HTML", content);
    }
}
