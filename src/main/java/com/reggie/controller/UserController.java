package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.reggie.common.BaseContext;
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
import javax.servlet.http.HttpServletRequest;
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
        //手机号码
        String phone = user.getPhone();
        if (phone != null) {
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //发送验证码
            log.info("验证码："+code);
            SMSUtils.sendMessage("验证码短信","SMS_251115011",phone,code);
            session.setAttribute(phone, code);
            return R.success("验证码发送成功");
        }

        return R.error("验证码发送失败");
    }


    @PostMapping("/login")
    public R<String> login(@RequestBody Map map, HttpServletRequest request,HttpSession session) {
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //验证码比对
        String Incode = session.getAttribute(phone).toString();
        if (code.equals(Incode)) {
            //判读是否为新用户
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone, phone);
            User one = userService.getOne(lambdaQueryWrapper);
            if (one == null) {
                one.setPhone(phone);
                one.setStatus(1);
                userService.save(one);
            }

            session.setAttribute("user",one.getId());
            return R.success("登录成功");
        }
        log.info("sa");
        return R.error("登录失败");
    }
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        //清理session中的用户id
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
