package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.dto.SetmealDto;
import com.reggie.entity.Setmeal;
import com.reggie.entity.SetmealDish;
import com.reggie.mapper.SetmealMapper;
import com.reggie.service.SetmealDishService;
import com.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Resource
    SetmealDishService setmealDishService;


    /***
     * 保存套餐和菜品基本信息
     */
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存基本信息
        this.save(setmealDto);
        //保存菜品关联信息
        for (SetmealDish sto : setmealDto.getSetmealDishes()) {
            sto.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
    }

    /***
     * 回显数据
     */
    @Override
    public SetmealDto getByIdWithSetmeal(Long id) {
        //获取普通信息
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        //获取菜品集合
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        List<SetmealDish> list = setmealDishService.list(lambdaQueryWrapper);
        setmealDto.setSetmealDishes(list);

        return setmealDto;
    }

    @Override
    public void updateSetmeal(SetmealDto setmealDto) {
        //更新基础信息
        this.updateById(setmealDto);
        //更新菜品列表
        //先删除原先的菜品列表
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(lambdaQueryWrapper);
        //在增加新的菜品
        setmealDto.getSetmealDishes().forEach(setmealDish -> setmealDish.setSetmealId(setmealDto.getId()));
        setmealDishService.updateBatchById(setmealDto.getSetmealDishes());
    }
}
