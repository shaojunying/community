package com.shao.community.dao;

import com.shao.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Author: shao
 * Date: 2020-09-19
 * Time: 13:33
 */
@Mapper
public interface LoginTicketMapper {

    @Insert(
            "insert into login_ticket(user_id, ticket, status, expired)" +
                    "values(#{userId}, #{ticket}, #{status}, #{expired})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LoginTicket loginTicket);


    @Select(
            "select id, user_id, ticket, status, expired from login_ticket " +
                    "where ticket=#{ticket}"
    )
    @ResultType(LoginTicket.class)
    LoginTicket selectByTicket(String ticket);

    @Update(
            "update login_ticket set status = #{status} where ticket=#{ticket}"
    )
    int updateStatusByTicket(String ticket, int status);

}
