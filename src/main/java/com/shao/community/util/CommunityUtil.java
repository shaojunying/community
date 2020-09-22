package com.shao.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import java.util.Map;
import java.util.UUID;

/**
 * @author shao
 * @date 2020-09-05 20:59
 */
public class CommunityUtil {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getCookie(Cookie[] cookies, String name) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static String convertToJson(int code, String message, Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("message", message);
        if (map != null && map.size() != 0) {
            jsonObject.put("data", map);
        }
        return jsonObject.toJSONString();
    }

    public static String convertToJson(int code, String message) {
        return convertToJson(code, message, null);
    }

    public static String convertToJson(int code) {
        return convertToJson(code, null);
    }

}
