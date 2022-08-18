package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;
import com.reggie.entity.DishFlavor;
import com.reggie.mapper.DishMapper;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
        //添加id
        dishDto.getFlavors().forEach(dishFlavor -> dishFlavor.setDishId(dishDto.getId()));
        service.saveBatch(dishDto.getFlavors());
        System.out.println(456789);
    }

    /***
     * 根据id查询菜品信息和口味信息
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //根据id查询菜品信息
        Dish dish = this.getById(id);

        //创建DTO保存信息
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //根据id查询口味信息
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        List<DishFlavor> DishFlavorList = service.list(lambdaQueryWrapper);

        dishDto.setFlavors(DishFlavorList);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新普通数据
        this.updateById(dishDto);
        //更新口味数据
        //移除数据
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(DishFlavor::getId,dishDto.getId());
        service.remove(lambdaQueryWrapper);
        dishDto.getFlavors().forEach(dishFlavor -> dishFlavor.setDishId(dishDto.getId()));

        //保存数据
        service.updateBatchById(dishDto.getFlavors());
    }
}
