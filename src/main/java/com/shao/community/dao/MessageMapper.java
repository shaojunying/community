package com.shao.community.dao;

import com.shao.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Message mapper.
 *
 * @author shao
 * @date 2020 /9/26 14:15
 */
@Mapper
public interface MessageMapper {

    /**
     * 与每个用户的最新一条消息,构成会话列表,分页
     *
     * @param userId the user id
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    List<Message> selectLatestMessagesWithEveryUser(int userId, int offset, int limit);

    /**
     * 会话列表数量
     *
     * @param userId the user id
     * @return the list
     */
    int selectLatestMessagesRowsWithEveryUser(int userId);

    /**
     * 与指定用户未读私信数量
     *
     * @param userId         the user id
     * @param conversationId the conversation id 为 null 时表示查询与所有用户的未读私信数量,否则查询与指定用户的私信数量
     * @return the int
     */
    int selectUnreadMessagesRows(@Param("userId") int userId,
                                 @Param("conversationId") String conversationId);

    /**
     * 与指定用户的私信,分页
     *
     * @param conversationId the conversation id
     * @param offset         the offset
     * @param limit          the limit
     * @return the int
     */
    List<Message> selectMessages(@Param("conversationId") String conversationId,
                                 @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 用指定用户私信总条数
     *
     * @param conversationId the conversation id
     * @return the list
     */
    int selectMessagesRows(@Param("conversationId") String conversationId);

    /**
     * Insert message int.
     *
     * @param message the message
     * @return the int
     */
    int insertMessage(Message message);

    /**
     * Select by id int.
     *
     * @param id the id
     * @return the int
     */
    Message selectById(@Param("id") int id);

    /**
     * Update status.
     *
     * @param id     the id
     * @param status the status
     * @return the int
     */
    int updateStatus(int id, int status);
}
