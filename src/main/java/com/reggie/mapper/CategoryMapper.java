package com.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * Category基本操作
 * @author shu
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
