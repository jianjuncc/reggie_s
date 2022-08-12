package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.CustomException;
import com.reggie.entity.Category;
import com.reggie.entity.Dish;
import com.reggie.entity.Setmeal;
import com.reggie.mapper.CategoryMapper;
import com.reggie.service.CategoryService;
import com.reggie.service.DishService;
import com.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * CategoryServiceImpl实现类
 *
 * @author shu
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    DishService dishService;
    @Resource
    SetmealService setmealService;


    /***
     * 根据id删除数据
     */
    @Override
    public void remove(long ids, int type) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (type == 1) {
            //当前分类是否关联菜品，有抛出异常
            dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
            long dishCount = dishService.count();
            if (dishCount > 0) {
                throw new CustomException("当前分类关联菜品，无法删除");
            }
        }else {
            //当前分类是否关联套餐，有抛出异常
            setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
            long setmealCount = setmealService.count(setmealLambdaQueryWrapper);
            if (setmealCount > 0) {
                throw new CustomException("当前分类关联套餐，无法删除");
            }
        }
        //无问题删除
        super.removeById(ids);
    }

}
