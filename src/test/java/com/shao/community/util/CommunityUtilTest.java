package com.shao.community.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shao
 * @date 2020/9/22 15:11
 */
public class CommunityUtilTest {
    @Test
    public void convertToJsonWithoutMessageAndMap() {
        String result = CommunityUtil.convertToJson(0);
        System.out.println(result);
        Assert.assertEquals("{\"code\":0}", result);
    }

    @Test
    public void convertToJsonWithoutMap() {
        String result = CommunityUtil.convertToJson(0, "成功");
        System.out.println(result);
        Assert.assertEquals("{\"code\":0,\"message\":\"成功\"}", result);
    }

    @Test
    public void convertToJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "shaojunying");
        String result = CommunityUtil.convertToJson(0, "成功", map);
        System.out.println(result);
        Assert.assertEquals("{\"code\":0,\"data\":{\"name\":\"shaojunying\"},\"message\":\"成功\"}", result);
    }
}
