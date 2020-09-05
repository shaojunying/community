package com.shao.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Author: shao
 * Date: 2020-09-04
 * Time: 22:11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class LoggerTest {
    Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void testLog() {
        logger.debug("debugLog");
        logger.info("infoLog");
        logger.warn("warnLog");
        logger.error("error");
    }


}
