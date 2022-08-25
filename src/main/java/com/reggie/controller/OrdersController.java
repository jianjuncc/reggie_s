package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.entity.Orders;
import com.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    OrderService orderService;
    @GetMapping("/page")
    public R<Page<Orders>> page(int page, int pageSize, Long number, @DateTimeFormat String beginTime, @DateTimeFormat String endTime){
        log.info("number:{},beginTime{},endTime{}",number,beginTime,endTime);
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(number !=null,Orders::getNumber,number);
        lambdaQueryWrapper.between(Orders::getOrderTime,beginTime,endTime);
        orderService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }
}
