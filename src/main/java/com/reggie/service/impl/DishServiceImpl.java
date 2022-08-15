package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;
import com.reggie.entity.DishFlavor;
import com.reggie.mapper.DishMapper;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Resource
    DishFlavorService service;
    /***
     * 新增菜品
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息到菜品表到dish
        this.save(dishDto);
        //菜品口味处理
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        //添加id
        dishDto.getFlavors().forEach(flavor->flavor.setDishId(dishId));
        log.info(dishDto.getFlavors().toString());
        service.saveBatch(dishDto.getFlavors());
        System.out.println(456789);
    }
}
