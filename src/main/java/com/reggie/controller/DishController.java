package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;
import com.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;


@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Resource
    DishService service;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page:{},pageSize:{}, String:{}", page, pageSize, name);
        //分页构造器
        Page pageInfo = new Page(page, pageSize);

        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        //排序时间
        lambdaQueryWrapper.orderByAsc(Dish::getUpdateTime);
        //将数据存放到页面
        service.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @PostMapping("/status/{type}")
    public R<String> status(@PathVariable int type, String[] ids){
        log.info(ids[0]);
        log.info("type{}",type);

        for (String s : ids) {
            Dish dish = service.getById(s);
            dish.setStatus(type);
            service.updateById(dish);
        }

        return R.success("更改状态成功");
    }
    @DeleteMapping
    public R<String> delete(String[] ids){
        for (String s : ids) {
            service.removeById(s);
        }
        return R.success("删除成功");
    }
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        service.saveWithFlavor(dishDto);
        return R.success("添加成功");
    }
}
