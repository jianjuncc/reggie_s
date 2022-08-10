package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.entity.Employee;
import com.reggie.service.impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 员工相关操作
 *
 * @author shu
 */
@RestController
@Slf4j
@RequestMapping(value = "/employee")
public class EmployeeController {

    @Autowired
    EmployeeServiceImpl  service;

    /***
     * 员工登录
     */
    @PostMapping(value = "/login")
    public R<Employee> login(Model model, HttpServletRequest httpRequest, @RequestBody Employee employee) {
        //1、将页面提交的密码password进行加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = service.getOne(queryWrapper);

        //3、如果没用查询到结果则返回登陆失败结果
        if (emp == null) {
            return R.error("登陆失败");
        }

        //4、密码对比，如果不一致则返回登陆失败
        if (!emp.getPassword().equals(password)) {
            return R.error("登陆失败,密码不对");
        }

        //5、查看员工状态，如果为已经禁用的状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("登陆失败，员工已禁用");
        }

        //登陆成功，将员工id存入session并返回登录成功结果
        model.addAttribute("employee",emp.getId());
        httpRequest.getSession().setAttribute("employee", emp.getId());
        httpRequest.getServletContext().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    @PostMapping(value = "/logout")
    public R<String> logout(HttpServletRequest request) {
        //移除session中的id属性
        request.getSession().removeAttribute("employee");
        return R.success("退出");
    }

    @PostMapping("/add")
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("员工属性{}", employee);
        // 第一步设置初始密码 123456 使用MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //第四步保存信息
        service.save(employee);
        return R.success("保存员工信息成功");
    }

    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name) {
        log.info("page:{},pageSize:{},name:{}",page,pageSize,name);
        //构造分页构造器
        Page<Employee> pageInfo = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Employee::getUpdateTime);
        //执行查询
        service.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(HttpServletResponse response, HttpServletRequest request, @RequestBody Employee employee) throws IOException {
        log.info(employee.toString());
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateUser(empId);
        employee.setUpdateTime(LocalDateTime.now());
        service.updateById(employee);
        return R.success("权限运行成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("查询员工信息");
        Employee employee = service.getById(id);
        return R.success(employee);
    }

}
