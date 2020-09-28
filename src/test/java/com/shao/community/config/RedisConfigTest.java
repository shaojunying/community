package com.shao.community.config;

import com.shao.community.CommunityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Redis config test.
 *
 * @author shao
 * @date 2020 /9/28 16:14
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommunityApplication.class)
@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 测试redis中字符串的操作
     */
    @Test
    public void testRedisString() {
        String key = "test:count";
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, 1);
        assertEquals(1, operations.get(key));
        assertEquals(2, operations.increment(key));
        assertEquals(1, operations.decrement(key));
    }

    /**
     * 测试redis中的hash
     */
    @Test
    public void testRedisHash() {
        String key = "test:student";
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        operations.put(key, "id", 1);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "shaojunying");
        map.put("gender", "male");
        operations.putAll(key, map);
        assertAll("测试redis中的hash操作", () -> {
            assertEquals(1, operations.get(key, "id"));
        }, () -> {
            assertEquals("shaojunying", operations.get(key, "name"));
        }, () -> {
            assertEquals("male", operations.get(key, "gender"));
        });
    }

    /**
     * 测试redis中的list
     */
    @Test
    public void testRedisList() {
        String key = "test:teacher";
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        operations.leftPush(key, 101);
        operations.leftPush(key, 102);
        operations.leftPush(key, 103);
        operations.rightPush(key, 104);
        operations.rightPush(key, 105);
        operations.rightPush(key, 106);
        assertAll("测试redis中的列表", () -> {
            assertEquals(6, operations.size(key));
        }, () -> {
            assertEquals(102, operations.index(key, 1));
        }, () -> {
            assertTrue(Objects.requireNonNull(operations.range(key, 0, 1))
                    .containsAll(Arrays.asList(103, 102)));
        }, () -> {
            assertEquals(103, operations.leftPop(key));
        }, () -> {
            assertEquals(102, operations.leftPop(key));
        }, () -> {
            assertEquals(101, operations.leftPop(key));
        }, () -> {
            assertEquals(104, operations.leftPop(key));
        }, () -> {
            assertEquals(105, operations.leftPop(key));
        }, () -> {
            assertEquals(106, operations.leftPop(key));
        }, () -> {
            assertEquals(0, operations.size(key));
        });
    }

    @Test
    public void testRedisSet() {
        String key = "test:set";
        SetOperations<String, Object> operations = redisTemplate.opsForSet();
        operations.add(key, "shao", "shao");
        operations.add(key, "junying");
        assertAll("Test Redis Set", () -> {
            assertTrue(operations.isMember(key, "shao"));
        }, () -> {
            assertEquals(3, operations.size(key));
        });
    }

    @Test
    public void testRedisSortedSet() {
        String key = "test:score";
        ZSetOperations<String, Object> operations = redisTemplate.opsForZSet();
        operations.add(key, "shao", 100);
        operations.add(key, "jun", 90);
        operations.add(key, "ying", 70);
        assertAll("Test redis sorted set", () -> {
            assertEquals(0, operations.reverseRank(key, "shao"));
        }, () -> {
            assertEquals(3, operations.zCard(key));
        });
    }

    @Test
    public void testKeys() {
        String key = "test:student";
        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, "shao");
        }
        assertTrue(redisTemplate.hasKey(key));
        redisTemplate.delete(key);
        assertFalse(redisTemplate.hasKey(key));
    }

    @Test
    public void testBoundOperation() {
        String key = "test:bound";
        BoundValueOperations<String, Object> operations = redisTemplate.boundValueOps(key);
        operations.set(1);
        assertEquals(1, operations.get());
    }

    @Test
    public void testTransaction() {
        Object object = redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String key = "test:tx";
                redisOperations.multi();
                redisOperations.opsForSet().add(key, "shao");
                redisOperations.opsForSet().add(key, "jun");
                redisOperations.opsForSet().add(key, "ying");
                System.out.println(redisOperations.opsForSet().members(key));
                return redisOperations.exec();
            }
        });
        System.out.println(object);
        System.out.println(redisTemplate.opsForSet().members("text:tx"));
    }

}
