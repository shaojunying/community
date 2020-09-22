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

}
