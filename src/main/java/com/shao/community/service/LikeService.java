package com.shao.community.service;

import com.shao.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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
     * @param entityType   被点赞实体的类型 (1 ==> 帖子 2 ==> 评论)
     * @param entityId     被点赞实体的id
     * @param userId       点赞用户的id
     * @param entityUserId 被点赞用户的id
     */
    public void likeOrUnlike(int entityType, int entityId, int userId, int entityUserId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                boolean likeStatus = Optional.ofNullable(operations.opsForSet().isMember(entityLikeKey, userId))
                        .orElseThrow(() -> new RuntimeException("获取点赞状态失败"));
                operations.multi();
                if (likeStatus) {
                    // 取消赞
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    // 赞
                    operations.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey);
                }
                return operations.exec();
            }
        });
    }

    public int getUserLikesCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        return Optional.ofNullable((Integer) redisTemplate.opsForValue().get(userLikeKey)).orElse(0);
    }

    public boolean getLikeStatus(int entityType, int entityId, int userId) {
        String key = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return Optional.ofNullable(redisTemplate.opsForSet().isMember(key, userId))
                .orElseThrow(() -> new RuntimeException("获取点赞状态失败"));
    }


}
