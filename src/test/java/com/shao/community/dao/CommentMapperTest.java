package com.shao.community.dao;

import com.shao.community.CommunityApplication;
import com.shao.community.entity.Comment;
import com.shao.community.util.CommunityConstant;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shao
 * @date 2020/9/25 9:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
@Transactional
public class CommentMapperTest {

    @Autowired
    CommentMapper commentMapper;

    @Test
    public void selectComments() {
        List<Comment> comments = commentMapper.selectComments(CommunityConstant.COMMENT_TO_POST, 228, 0, 100);
        System.out.println(comments);
        Assert.assertNotNull(comments);
    }

    @Test
    public void selectCommentsRows() {
        int rows = commentMapper.selectCommentsRows(CommunityConstant.COMMENT_TO_POST, 228);
        System.out.println(rows);
        Assert.assertNotEquals(0, rows);
    }
}
