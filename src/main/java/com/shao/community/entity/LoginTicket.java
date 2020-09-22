package com.shao.community.entity;

import java.util.Date;

/**
 * @author shao
 * @date 2020-09-19 13:30
 */
public class LoginTicket {

    private int id;
    private int userId;
    private String ticket;

    /**
     * 当前凭证是否有效
     * 0 ==> 有效
     * 1 ==> 无效
     */
    private int status;
    private Date expired;

    public LoginTicket() {
    }

    public LoginTicket(int userId, String ticket, int status, Date expired) {
        this.userId = userId;
        this.ticket = ticket;
        this.status = status;
        this.expired = expired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "LoginTicket{" +
                "id=" + id +
                ", userId=" + userId +
                ", ticket='" + ticket + '\'' +
                ", status=" + status +
                ", expired=" + expired +
                '}';
    }
}
