package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;
import com.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Resource
    DishService dishService;

    @Resource
    RedisTemplate<Object,Object> template;
    @GetMapping("/page")
    public R<Page<Dish>> page(int page, int pageSize, String name) {
        log.info("page:{},pageSize:{}, String:{}", page, pageSize, name);
        //分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        //排序时间
        lambdaQueryWrapper.orderByAsc(Dish::getUpdateTime);
        //将数据存放到页面
        dishService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @PostMapping("/status/{type}")
    public R<String> status(@PathVariable int type, String[] ids){
        log.info(ids[0]);
        log.info("type{}",type);

        for (String s : ids) {
            Dish dish = dishService.getById(s);
            dish.setStatus(type);
            dishService.updateById(dish);
        }

        return R.success("更改状态成功");
    }
    @DeleteMapping
    public R<String> delete(String[] ids){
        for (String s : ids) {
            dishService.removeById(s);
        }
        return R.success("删除成功");
    }
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        Set<Object> keys = template.keys("dish_*");
        template.delete(keys);
        return R.success("添加成功");
    }

    /***
     * 根据id查询菜品信息和口味信息
     */
    @GetMapping("/{id}")
    public R<DishDto> modify(@PathVariable Long id){
        log.info("id={}",id);
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        Set<Object> keys = template.keys("dish_*");
        template.delete(keys);
        return R.success("更新成功");
    }

    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        //创造key
        String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        List<Dish> list = (List<Dish>) template.opsForValue().get(key);
        if (list != null) {
            R.success(list);
        }
        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        list = dishService.list(lambdaQueryWrapper);
        template.opsForValue().set(key,list,1, TimeUnit.HOURS);
        return R.success(list);
    }
}
