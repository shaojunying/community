package com.shao.community.interceptor;

import com.shao.community.entity.LoginTicket;
import com.shao.community.entity.User;
import com.shao.community.service.MessageService;
import com.shao.community.service.UserService;
import com.shao.community.util.CommunityUtil;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author shao
 * @date 2020-09-20 15:27
 */
@Component
public class UserServiceInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 通过ticket获取User信息并存储起来
        String ticket = CommunityUtil.getCookie(request.getCookies(), "ticket");
        if (ticket != null) {
            LoginTicket loginTicket = userService.getLoginTicketByTicket(ticket);
            if (isValidLoginTicket(loginTicket)) {
                User user = userService.findUserById(loginTicket.getUserId());
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 判断hostHandle中是否存在User,如果存在,赋给modelAndView
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject(user);
            int unreadRows = messageService.selectUnreadMessagesRows(user.getId(), null);
            modelAndView.addObject("unreadRows", unreadRows);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.removeUser();
    }

    private boolean isValidLoginTicket(LoginTicket ticket) {
        return ticket != null && ticket.getStatus() != 1 && ticket.getExpired().after(new Date());
    }
}
