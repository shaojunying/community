package com.shao.community.util;

import com.shao.community.CommunityApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Author: shao
 * Date: 2020-09-21
 * Time: 21:32
 */
@ContextConfiguration(classes = CommunityApplication.class)
@SpringBootTest
@RunWith(SpringRunner.class)
public class TrieTreeUtilTest {

    @Autowired
    TrieTreeUtil trieTreeUtil;

    @Test
    public void filter1() {

        String string = "不吸毒,不赌博,不喝酒";
        String filter = trieTreeUtil.filter(string);
        System.out.println(filter);
        Assert.assertEquals("不***,不***,不喝酒", filter);
    }

    @Test
    public void filter2() {

        String string = "不吸毒,不赌博,不喝酒.";
        String filter = trieTreeUtil.filter(string);
        System.out.println(filter);
        Assert.assertEquals("不***,不***,不喝酒.", filter);
    }

    @Test
    public void filter3() {

        String string = "不*吸*毒,不-赌&博,不喝8酒.";
        String filter = trieTreeUtil.filter(string);
        System.out.println(filter);
        Assert.assertEquals("不****,不-***,不喝8酒.", filter);
    }
}
