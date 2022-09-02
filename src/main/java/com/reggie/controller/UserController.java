package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.reggie.common.R;
import com.reggie.entity.User;
import com.reggie.service.UserService;
import com.reggie.utils.SMSUtils;
import com.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        //生成验证码
        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info(code);
            //发送验证码
            SMSUtils.sendMessage("瑞吉外卖", "", phone, code);
            //session保存验证码
            session.setAttribute(phone,code);
            return R.success("手机验证码发送成功");
        }
        return R.error("验证码发送失败");
    }


    @PostMapping("/login")
    public R<String> login(@RequestBody User user, HttpSession session, Map map) {
        //获取手机号
        String phone = user.getPhone();
        //获取验证码
        String code = map.get("code").toString();
        //从session中获取验证码
        String Incode = session.getAttribute(phone).toString();
        //比对验证码
        if (code.equals(Incode)) {
            //判断是否为新用户
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone,phone);
            User one = userService.getOne(lambdaQueryWrapper);
            if (one == null) {
                one.setPhone(user.getPhone());
                userService.save(one);
            }

            return R.success("登录成功");
        }


        return R.error("登录失败");
    }
}
