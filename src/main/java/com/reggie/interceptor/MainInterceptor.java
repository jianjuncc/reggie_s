package com.reggie.interceptor;

import com.reggie.common.R;
import lombok.val;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author shu
 */
@Component
public class MainInterceptor implements HandlerInterceptor {
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       System.out.println("1");
       // 检查 session 中是否有user对象存在
       try {
           HttpSession session = request.getSession();
           Long empId = (Long) session.getAttribute("employee");
           if (empId != null) {
               return true;
           }
           R.error("未知错误");
       } catch (Exception e) {
           throw new RuntimeException(e);
       }

       return false;
   }

     public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
         System.out.println("2");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        System.out.println("3");
    }
}
