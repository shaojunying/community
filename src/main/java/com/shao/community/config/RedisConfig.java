package com.shao.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author shao
 * @date 2020/9/28 16:08
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // key的序列化方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        // value的序列化方式
        redisTemplate.setValueSerializer(RedisSerializer.json());
        // hash的key的序列化方式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // hash的value的序列化方式
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
