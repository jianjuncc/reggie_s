package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;
import com.reggie.entity.DishFlavor;
import com.reggie.mapper.DishMapper;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired(required=false)
    DishFlavorService service;
    /***
     * 新增菜品
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息到菜品表到dish
        this.save(dishDto);
        //菜品口味处理
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        //添加id
        for (DishFlavor dishFlavor : flavors) {
            dishFlavor.setDishId(dishId);
        }

        service.saveBatch(flavors);
    }
}
