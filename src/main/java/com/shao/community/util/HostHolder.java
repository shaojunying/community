package com.shao.community.util;

import com.shao.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author shao
 * @date 2020-09-20 19:52
 * @description 持有用户信息, 代替了session的功能
 * 本来需要将user的信息存入session
 * 现在通过客户端存的cookie在数据库中查询用户的信息,减轻服务器的压力
 */
@Component
public class HostHolder {
    private final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public User getUser() {
        return userThreadLocal.get();
    }

    public void setUser(User user) {
        userThreadLocal.set(user);
    }

    public void removeUser() {
        userThreadLocal.remove();
    }
}
