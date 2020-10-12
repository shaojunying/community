package com.shao.community.service;

import com.shao.community.dao.UserMapper;
import com.shao.community.entity.User;
import com.shao.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author shao
 * @date 2020/10/11 19:14
 */
@Service
public class FollowService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserMapper userMapper;

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

    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return Optional.ofNullable(redisTemplate.opsForZSet().zCard(followeeKey))
                .orElseThrow(() -> new RuntimeException("查询失败"));
    }

    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return Optional.ofNullable(redisTemplate.opsForZSet().zCard(followerKey))
                .orElseThrow(() -> new RuntimeException("查询失败"));
    }

    public boolean isFollowed(int entityType, int entityId, int userId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().score(followerKey, userId) != null;
    }

    /**
     * Gets followee.
     *
     * @param userId     the user id
     * @param entityType the entity type
     * @param offset     the offset
     * @param limit      the limit
     * @return the followee
     */
    public List<Map<String, Object>> getFollowee(int userId, int entityType, int offset, int limit) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getFollowEntity(offset, limit, followeeKey);
    }

    /**
     * 获取当前实体的粉丝列表
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @param offset     the offset
     * @param limit      the limit
     * @return the followee
     */
    public List<Map<String, Object>> getFollower(int entityType, int entityId, int offset, int limit) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getFollowEntity(offset, limit, followerKey);
    }

    private List<Map<String, Object>> getFollowEntity(int offset, int limit, String key) {
        Set<Object> objects = redisTemplate.opsForZSet().reverseRange(key, offset, offset + limit - 1);
        List<Map<String, Object>> ans = new LinkedList<>();
        if (objects != null) {
            for (Object object : objects) {
                Map<String, Object> map = new HashMap<>();
                Integer userId = (Integer) object;
                User user = userMapper.selectById(userId);
                map.put("user", user);
                Double score = redisTemplate.opsForZSet().score(key, object);
                if (score == null) {
                    throw new RuntimeException("获取关注信息失败");
                }
                map.put("followTime", new Date(score.longValue()));
                ans.add(map);
            }
        }
        return ans;
    }

}
