package com.shao.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shao
 * @date 2020/10/16 19:11
 */
public class Event {
    private final Map<String, Object> data = new HashMap<>();
    /**
     * 事件对应的topic,指定将该事件发送到哪个topic
     */
    private String topic;
    /**
     * 动作发起者id
     */
    private int userId;
    /**
     * 被操作的实体类型
     */
    private int entityType;
    /**
     * 被操作的实体id
     */
    private int entityId;
    /**
     * 被操作者id
     */
    private int entityUserId;

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
