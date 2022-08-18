package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.entity.Setmeal;
import com.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Resource
    SetmealService service;
    @GetMapping("/page")
    public R<Page<Setmeal>> page(int page, int pageSize, String name){
        //构造分页器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        setmealLambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);
        //调用构造器
        service.page(pageInfo,setmealLambdaQueryWrapper);
        return R.success(pageInfo);
    }
    @PostMapping("/status/{type}")
    public R<String> status(@PathVariable int type, String[] ids){
        for (String id : ids) {
            Setmeal setmeal = service.getById(id);
            setmeal.setStatus(type);
            service.updateById(setmeal);
        }
        return R.success("状态更改成功");
    }

    @DeleteMapping
    public R<String> delete(String[] ids){
        for (String id : ids) {
            service.removeById(id);
        }
        return R.success("删除成功");
    }
}
