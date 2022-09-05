package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.CustomException;
import com.reggie.common.R;
import com.reggie.dto.SetmealDto;
import com.reggie.entity.Setmeal;
import com.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Resource
    private SetmealService setmealService;


    @GetMapping("/page")
    public R<Page<Setmeal>> page(int page, int pageSize, String name){
        //构造分页器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        setmealLambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);
        //调用构造器
        setmealService.page(pageInfo,setmealLambdaQueryWrapper);
        return R.success(pageInfo);
    }
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }
    @PostMapping("/status/{type}")
    public R<String> status(@PathVariable int type, String[] ids){
        for (String id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(type);
            setmealService.updateById(setmeal);
        }
        return R.success("状态更改成功");
    }

    /***
     * 回显数据
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getByid(@PathVariable long id){
        log.info("id:{}",id);
        SetmealDto byIdWithSetmeal = setmealService.getByIdWithSetmeal(id);
        return R.success(byIdWithSetmeal);
    }

    /***
     * 更新数据
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateSetmeal(setmealDto);
        return R.success("更新数据成功");
    }
    @DeleteMapping
    public R<String> delete(String[] ids){
        for (String id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            if (setmeal.getStatus() == 0) {
                setmealService.removeById(id);
            }else {
                throw new CustomException("状态不对，套餐正在售卖");
            }
        }
        return R.success("删除成功");
    }
}
