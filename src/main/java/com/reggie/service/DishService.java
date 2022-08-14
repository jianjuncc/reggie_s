package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;
import org.springframework.stereotype.Service;

@Service
public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
}
