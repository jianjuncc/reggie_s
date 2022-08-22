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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

        List<SetmealDish> collect = setmealDto.getSetmealDishes().stream().peek(item -> {
            // 设置套餐id
            item.setSetmealId(setmealDto.getId());
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(collect);
    }

    /***
     * 回显数据
     */
    @Override
    @Transactional
    public SetmealDto getByIdWithSetmeal(Long id) {
        //id查询
        Setmeal setmeal = this.getById(id);
        //创建目标数据
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        //复制dish信息
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(true, SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(lambdaQueryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    public void updateSetmeal(SetmealDto setmealDto) {
        //更新数据
        this.updateById(setmealDto);
        //更新数据
        //删除原来数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getDishId,setmealDto.getId());
        setmealDishService.remove(lambdaQueryWrapper);

        setmealDto.getSetmealDishes().forEach(setmealDish -> setmealDish.setSetmealId(setmealDto.getId()));

        //保存数据
        setmealDishService.updateBatchById(setmealDto.getSetmealDishes());
    }


}
