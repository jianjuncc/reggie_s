package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.common.R;
import com.reggie.entity.Employee;
import com.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
@RequestMapping(value = "/employee", method = RequestMethod.POST)
public class EmployeeController {

    @Resource
    EmployeeService service;

    /***
     * 员工登录
     * @param httpRequest
     * @param employee
     * @return
     */
    @PostMapping(value = "/login")
    public R<Employee> login(HttpServletRequest httpRequest, @RequestBody Employee employee){
        //1、将页面提交的密码password进行加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
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
        if (emp.getStatus() == 0){
            return R.error("登陆失败，员工已禁用");
        }

        //登陆成功，将员工id存入session并返回登录成功结果
        httpRequest.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    @PostMapping(value = "/logout")
    public R logout(HttpServletRequest request){
        //移除session中的id属性
        request.getSession().removeAttribute("employee");
        return R.success("退出");
    }
}
