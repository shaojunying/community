package com.shao.community.controller;

import com.shao.community.entity.LoginTicket;
import com.shao.community.entity.User;
import com.shao.community.service.UserService;
import com.shao.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Author: shao
 * Date: 2020-09-20
 * Time: 15:27
 */
@Component
public class UserServiceInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 获取ticket的cookie
        String ticket = CommunityUtil.getCookie(request.getCookies(), "ticket");
        if (ticket != null) {
            LoginTicket loginTicket = userService.getLoginTicketByTicket(ticket);
            if (isValidLoginTicket(loginTicket)) {
                User user = userService.findUserById(loginTicket.getUserId());
                if (user != null) {
                    modelAndView.addObject(user);
                }
            }
        }
    }

    private boolean isValidLoginTicket(LoginTicket ticket) {
        return ticket != null && ticket.getStatus() != 1 && ticket.getExpired().after(new Date());
    }
}
