package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.entity.Category;
import com.reggie.entity.Setmeal;
import com.reggie.service.CategoryService;
import com.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜品分类
 *
 * @author shu
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Resource
    CategoryService service;

    @Resource
    SetmealService setmealService;

    /***
     * 分页查询
     */
    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize) {
        log.info("page:{},pageSize:{}", page, pageSize);
        //构建构造分页
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //排序
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        //存入查询
        service.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /***
     * 新增分类
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("新增分类{}", category);
        service.save(category);
        return R.success("新增分类成功");
    }

    /***
     * 根据id删除分类
     */
    @DeleteMapping
    public R<String> delete(@RequestParam Long ids) {
        log.info("id值{}", ids);
        Integer type = service.getById(ids).getType();
        service.remove(ids, type);
        return R.success("删除成功");
    }

    /***
     * 更新数据
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info(category.getName());
        service.updateById(category);
        return R.success("更新成功");
    }

    /***
     * 按值查询
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //排序
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        List<Category> list = service.list(lambdaQueryWrapper);
        return R.success(list);
    }


}
