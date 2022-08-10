package com.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
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
    @Override
    public void insertFill(MetaObject metaObject) {

        log.info("insert");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());}

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",  request.getSession().getAttribute("employee"));
    }
}
