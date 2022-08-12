package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.entity.Dish;
import com.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Resource
    DishService service;
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize, String name){
        log.info("page:{},pageSize:{}, String:{}",page,pageSize,name);
        //分页构造器
        Page pageInfo = new Page(page,pageSize);

        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName, name);
        //排序时间
        lambdaQueryWrapper.orderByAsc(Dish::getUpdateTime);
        //将数据存放到页面
        service.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status, String[] ids){
        log.info(ids.toString());
        for (String id :ids) {
            Dish dish = service.getById(id);
            dish.setStatus(status);
            service.updateById(dish);
        }
        return R.success("状态改变成功");
    }
}
