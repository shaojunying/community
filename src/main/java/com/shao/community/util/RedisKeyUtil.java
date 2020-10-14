package com.shao.community.util;

/**
 * The type Redis key util.
 *
 * @author shao
 * @date 2020 /10/10 17:10
 */
public class RedisKeyUtil {
    private final static String SPLIT = ":";
    private final static String ENTITY_LIKE_PREFIX = "like" + SPLIT + "entity";
    private final static String USER_LIKE_PREFIX = "like" + SPLIT + "user";
    /**
     * 用户的粉丝
     */
    private final static String FOLLOWEE_PREFIX = "followee";
    /**
     * 用户的关注
     */
    private final static String FOLLOWER_PREFIX = "follower";
    private final static String KAPTCHA_PREFIX = "kaptcha";
    private final static String TICKET_PREFIX = "ticket";
    private final static String USER_PREFIX = "user";

    /**
     * 获取实体点赞对应的key
     * like:entity:entityType:entityId ==> set(userId)
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @return the entity like key
     */
    public static String getEntityLikeKey(int entityType, int entityId) {
        return ENTITY_LIKE_PREFIX + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * Gets user like key.
     *
     * @param entityUserId the entity user id
     * @return the user like key
     */
    public static String getUserLikeKey(int entityUserId) {
        return USER_LIKE_PREFIX + SPLIT + entityUserId;
    }

    /**
     * 获取某个用户关注的实体信息
     * 此时用户是关注者，实体是被关注者
     * followee:userId:entityType ==> zset(entityId)
     *
     * @param userId     用户关注的实体信息
     * @param entityType 实体类型
     * @return string string
     */
    public static String getFolloweeKey(int userId, int entityType) {
        return FOLLOWEE_PREFIX + SPLIT + userId + SPLIT + entityType;
    }

    /**
     * 某个实体获得的关注信息
     * follower:entityType:entityId ==> zset(userId)
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return string
     */
    public static String getFollowerKey(int entityType, int entityId) {
        return FOLLOWER_PREFIX + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getKaptchaKey(String owner) {
        return KAPTCHA_PREFIX + SPLIT + owner;
    }

    public static String getTicketKey(String ticket) {
        return TICKET_PREFIX + SPLIT + ticket;
    }

    public static String getUserKey(int userId) {
        return USER_PREFIX + SPLIT + userId;
    }
}
