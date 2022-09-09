package com.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.spi.http.HttpContext;
import java.time.LocalDateTime;

/**
 * 注解处理器
 * @author shu
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpSession session;
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        if (session.getAttribute("user").toString() != null) {
            metaObject.setValue("createUser", session.getAttribute("user"));
            metaObject.setValue("updateUser", session.getAttribute("user"));
        }else {
            metaObject.setValue("createUser", session.getAttribute("employee"));
            metaObject.setValue("updateUser", session.getAttribute("employee"));
        }

    }
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        if (session.getAttribute("user").toString() != null) {
            metaObject.setValue("updateUser", session.getAttribute("user"));
        }else {
            metaObject.setValue("updateUser",  request.getSession().getAttribute("employee"));
        }
    }
}
