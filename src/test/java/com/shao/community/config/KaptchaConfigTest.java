package com.shao.community.config;

import com.google.code.kaptcha.Producer;
import com.shao.community.CommunityApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Author: shao
 * Date: 2020-09-14
 * Time: 16:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class KaptchaConfigTest {
    @Autowired
    Producer producer;

    @Test
    public void kaptchaProducer() throws IOException {
        String text = producer.createText();
        System.out.println(text);
        Assert.assertEquals(text.length(), 4);
        BufferedImage image = producer.createImage(text);
        File outputFile = new File("kaptcha.jpg");
        ImageIO.write(image, "jpg", outputFile);
        System.out.println("保存验证码成功");
    }
}
