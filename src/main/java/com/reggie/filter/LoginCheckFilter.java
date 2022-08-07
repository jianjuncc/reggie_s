package com.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //拦截匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1、获取本次请求的url
        String requestURL = request.getRequestURI();
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "backend/**",
                "front/**"
        };
        // 2、判断本次请求是否需要处理
        boolean check = check(urls, requestURL);

        // 3、如果不需要处理，则直接放行
        if (check) {
            log.info("不需要处理");
            filterChain.doFilter(request,response);
            return;
        }
        // 4、判断登录状态，如果已经登录，则直接放行
         if (request.getSession().getAttribute("employee") != null){
             log.info("已经登录获取用户id"+request.getSession().getAttribute("employee"));
             filterChain.doFilter(request,response);
             return;
         }

        log.info("登陆失败");
         // 5、如果未登录则返回未登录结果
        response.getWriter().write(JSON.toJSONString(R.error("登录失败")));
    }

    /***
     * 查询匹配
     */
    public boolean check(String[] urls, String requestURL) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURL);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
