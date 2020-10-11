package com.shao.community.service;

import com.shao.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @author shao
 * @date 2020/10/11 19:14
 */
@Service
public class FollowService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户关注指定实体
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @param userId     the user id
     */
    public void follow(int entityType, int entityId, int userId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                operations.multi();
                long currentTimeMillis = System.currentTimeMillis();
                operations.opsForZSet().add(followeeKey, entityId, currentTimeMillis);
                operations.opsForZSet().add(followerKey, userId, currentTimeMillis);
                return operations.exec();
            }
        });
    }

    /**
     * 用户要取消关注指定实体
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @param userId     the user id
     */
    public void unfollow(int entityType, int entityId, int userId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                operations.multi();
                operations.opsForZSet().remove(followeeKey, entityId);
                operations.opsForZSet().remove(followerKey, userId);
                return operations.exec();
            }
        });
    }

}
