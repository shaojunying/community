package com.shao.community.event;

import com.shao.community.CommunityApplication;
import com.shao.community.entity.Event;
import com.shao.community.util.CommunityConstant;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author shao
 * @date 2020/10/16 19:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class ProduceEventTest {

    @Autowired
    private ProduceEvent produceEvent;

    @Test
    void fireEvent() throws InterruptedException {
        Event event = new Event().
                setTopic(CommunityConstant.LIKE_TOPIC)
                .setEntityType(CommunityConstant.COMMENT_TO_COMMENT)
                .setEntityId(242)
                .setEntityUserId(168);
        produceEvent.fireEvent(event);
        Thread.sleep(1000 * 10);
    }
}
