package com.shao.community.util;

/**
 * @author shao
 * @date 2020/10/10 17:10
 */
public class RedisKeyUtil {
    private final static String SPLIT = ":";
    private final static String ENTITY_LIKE_PREFIX = "like" + SPLIT + "entity";

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

}
