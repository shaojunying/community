package com.shao.community.service;

import com.shao.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author shao
 * @date 2020/10/10 17:06
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取当前实体的被点赞数量
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @return the int
     */
    public Long getLikesCount(int entityType, int entityId) {
        String key = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 点赞或取消点赞
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @param userId     the user id
     */
    public void likeOrUnlike(int entityType, int entityId, int userId) {
        String key = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        BoundSetOperations<String, Object> operations = redisTemplate.boundSetOps(key);
        boolean liked = getLikeStatus(entityType, entityId, userId);
        if (liked) {
            operations.remove(userId);
        } else {
            operations.add(userId);
        }
    }

    public boolean getLikeStatus(int entityType, int entityId, int userId) {
        String key = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return Optional.ofNullable(redisTemplate.opsForSet().isMember(key, userId))
                .orElseThrow(() -> new RuntimeException("获取点赞状态失败"));
    }


}
