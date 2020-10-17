package com.shao.community.interceptor;

import com.shao.community.entity.User;
import com.shao.community.service.MessageService;
import com.shao.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shao
 * @date 2020/10/17 17:05
 */
@Component
public class MessageInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user == null) {
            return;
        }
        int unreadMessagesRows = messageService.selectUnreadMessagesRows(user.getId(), null);
        int unreadNoticesCount = messageService.selectUnreadNoticesCount(user.getId(), null);
        modelAndView.addObject("allUnreadCount", unreadMessagesRows + unreadNoticesCount);
    }
}
