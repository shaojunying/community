package com.shao.community.event;

import com.alibaba.fastjson.JSONObject;
import com.shao.community.entity.DiscussPost;
import com.shao.community.entity.Event;
import com.shao.community.entity.Message;
import com.shao.community.service.DiscussPostService;
import com.shao.community.service.ElasticsearchService;
import com.shao.community.service.MessageService;
import com.shao.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shao
 * @date 2020/10/16 19:21
 */
@Component
public class ConsumeEvent {

    private final static Logger logger = LoggerFactory.getLogger(ConsumeEvent.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @KafkaListener(topics = {CommunityConstant.LIKE_TOPIC,
            CommunityConstant.COMMENT_TOPIC, CommunityConstant.FOLLOW_TOPIC})
    public void handleMessage(ConsumerRecord<String, String> consumerRecord) {
        if (consumerRecord == null || consumerRecord.value() == null) {
            throw new InvalidParameterException("消息内容为空!");
        }
        Event event = JSONObject.parseObject(consumerRecord.value(), Event.class);
        if (event == null) {
            throw new InvalidParameterException("解析json字符串consumerRecord.value()出错");
        }
        // 新消息对象,存储事件信息
        Message message = new Message();
        message.setCreateTime(new Date());
        message.setToId(event.getEntityUserId());
        message.setFromId(CommunityConstant.SYSTEM_USER_ID);
        message.setConversationId(event.getTopic());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.insertMessage(message);
    }

    @KafkaListener(topics = {CommunityConstant.PUBLISH_TOPIC})
    public void saveDiscussPostToElasticsearch(ConsumerRecord<String, String> consumerRecord) {
        if (consumerRecord == null || consumerRecord.value() == null) {
            throw new InvalidParameterException("消息内容为空!");
        }
        Event event = JSONObject.parseObject(consumerRecord.value(), Event.class);
        if (event == null) {
            throw new InvalidParameterException("解析json字符串consumerRecord.value()出错");
        }
        int postId = event.getEntityId();
        DiscussPost discussPost = discussPostService.findDiscussPost(postId);
        if (discussPost == null) {
            throw new RuntimeException("要查询的帖子不存在");
        }
        elasticsearchService.save(discussPost);
    }
}
