package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.entity.Category;
import org.springframework.stereotype.Service;

/**
 * Category服务类
 * @author shu
 */
@Service
public interface CategoryService extends IService<Category> {
    public void remove(long ids,int type);

}
