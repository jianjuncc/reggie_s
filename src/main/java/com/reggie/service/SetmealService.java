package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.DishDto;
import com.reggie.dto.SetmealDto;
import com.reggie.entity.Setmeal;
import org.springframework.stereotype.Service;

@Service
public interface SetmealService extends IService<Setmeal> {
    /***
     * 保存套餐和菜品基本信息
     */
    void saveWithDish(SetmealDto setmealDto);

    /***
     * 查询套餐信息
     */
    SetmealDto getByIdWithSetmeal(Long id);
    /***
     * 更新套餐信息
     */
    void updateSetmeal(SetmealDto setmealDto);
}
