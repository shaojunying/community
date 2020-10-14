package com.shao.community.service;

import com.shao.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author shao
 * @date 2020/10/14 16:02
 */
@Service
public class HomeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 将cookie==>code存储到redis中
     *
     * @param owner the owner
     * @param code  the code
     */
    public void saveKaptchaToRedis(String owner, String code) {
        String kaptchaKey = RedisKeyUtil.getKaptchaKey(owner);
        redisTemplate.opsForValue().set(kaptchaKey, code, 60, TimeUnit.SECONDS);
    }

    /**
     * 从redis中获取cookie对应的code
     *
     * @param owner the owner
     * @return the string
     */
    public String getKaptchaFromRedis(String owner) {
        String kaptchaKey = RedisKeyUtil.getKaptchaKey(owner);
        return (String) redisTemplate.opsForValue().get(kaptchaKey);
    }

}
