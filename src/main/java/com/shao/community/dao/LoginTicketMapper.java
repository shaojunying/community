package com.shao.community.dao;

import com.shao.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author shao
 * @date 2020-09-19 13:33
 */
@Mapper
public interface LoginTicketMapper {


    /**
     * 插入一条登录凭证
     *
     * @param loginTicket
     * @return
     */
    @Insert(
            "insert into login_ticket(user_id, ticket, status, expired)" +
                    "values(#{userId}, #{ticket}, #{status}, #{expired})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LoginTicket loginTicket);


    /**
     * 通过凭证查询一条登录凭证
     *
     * @param ticket
     * @return
     */
    @Select(
            "select id, user_id, ticket, status, expired from login_ticket " +
                    "where ticket=#{ticket}"
    )
    @ResultType(LoginTicket.class)
    LoginTicket selectByTicket(String ticket);

    /**
     * 更新凭证状态
     *
     * @param ticket
     * @param status 凭证状态 0代表有效,1代表无效
     * @return
     */
    @Update(
            "update login_ticket set status = #{status} where ticket=#{ticket}"
    )
    int updateStatusByTicket(String ticket, int status);

}
