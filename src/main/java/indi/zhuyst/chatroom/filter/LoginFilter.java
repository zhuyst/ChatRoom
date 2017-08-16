package indi.zhuyst.chatroom.filter;

import indi.zhuyst.chatroom.pojo.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by zhuyst on 2017/7/2.
 */
public class LoginFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest hRequest = (HttpServletRequest) servletRequest;
        User user = (User) hRequest.getSession().getAttribute("user");

        String requestPath = hRequest.getServletPath();
        if(user == null && !requestPath.equals("/") && !requestPath.equals("/login")
                && !requestPath.contains("/static/")){
            hRequest.getRequestDispatcher("/").forward(servletRequest,servletResponse);
        }
        else {
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {
    }
}
