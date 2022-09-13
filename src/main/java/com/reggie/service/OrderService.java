package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {
    /**
     * 下单
     */
    void submit(Orders orders);
}
