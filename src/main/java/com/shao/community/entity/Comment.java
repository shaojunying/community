package com.shao.community.entity;

import java.util.Date;

/**
 * @author shao
 * @date 2020/9/25 9:10
 */
public class Comment {

    private int id;
    private int userId;
    /**
     * 回复对象的类型
     * 1 代表帖子
     * 2 代表评论
     */
    private int entityType;
    /**
     * 回复对象的id
     */
    private int entityId;
    /**
     * 被回复人的id
     * 在回复评论时用来记录被回复人的id
     */
    private int targetId;
    private String content;
    /**
     * 评论的状态
     * 0 ==> 正常
     * 1 ==> 被禁用了
     */
    private int status;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", entityType=" + entityType +
                ", entityId=" + entityId +
                ", targetId=" + targetId +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
