package com.shao.community.util;

/**
 * @author shao
 * @date 2020-09-14 14:58
 */
public interface CommunityConstant {

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 重复激活
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态下ticket存储的时间(秒)
     */
    int DEFAULT_EXPIRED_SECOND = 3600 * 10;

    /**
     * 记住转台下ticket存储的时间(秒)
     */
    int REMEMBER_EXPIRED_SECOND = 3600 * 24 * 100;

    int COMMENT_TO_POST = 1;
    int COMMENT_TO_COMMENT = 2;
    int COMMENT_TO_USER = 3;

    String LIKE_TOPIC = "like";
    String COMMENT_TOPIC = "comment";
    String FOLLOW_TOPIC = "follow";
    String PUBLISH_TOPIC = "publish";

    int SYSTEM_USER_ID = 1;
}
