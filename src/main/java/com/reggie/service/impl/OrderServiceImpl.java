package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.CustomException;
import com.reggie.entity.AddressBook;
import com.reggie.entity.Orders;
import com.reggie.entity.ShoppingCart;
import com.reggie.entity.User;
import com.reggie.mapper.OrderMapper;
import com.reggie.service.AddressBookService;
import com.reggie.service.OrderService;
import com.reggie.service.ShoppingCartService;
import com.reggie.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Resource
    HttpSession session;

    @Resource
    ShoppingCartService cartService;

    @Resource
    AddressBookService addressBookService;

    @Resource
    UserService userService;

    /**
     * 下单
     */
    @Override
    @Transactional
    public void submit(Orders orders) {
        //下单的用户
        Long userId = (Long) session.getAttribute("user");
        User user = userService.getById(userId);
        //下单中的物品
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> list = cartService.list(lambdaQueryWrapper);
        if (list == null || list.size() == 0) {
            throw new CustomException("购物车数据不对，无法下单");
        }
        //下单的地址
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null) {
            throw new CustomException("用户地址信息，有误");
        }

    }
}
