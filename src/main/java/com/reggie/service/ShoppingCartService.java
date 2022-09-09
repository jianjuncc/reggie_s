package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public interface ShoppingCartService extends IService<ShoppingCart> {

    void clean(HttpSession session);
}
