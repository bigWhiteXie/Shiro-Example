package com.codexie.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("userController")
public class UserController {
    @Autowired


    @RequestMapping("login")
    public String login(){
        return "login";
    }

    @RequestMapping("userLogin")
    public String userLogin(String uname,String pwd,@RequestParam(defaultValue = "false") Boolean rm){
        //1.创建token对象
        UsernamePasswordToken token = new UsernamePasswordToken(uname, pwd,rm);
        //2.获取subject对象
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        session.setAttribute("user",uname);
        //3.执行登录
        subject.login(token);
        //4.页面跳转到main
        return "main";
    }

    @RequestMapping("psCheck")
    @ResponseBody
    public String psCheck(){
        return "权限认证成功";
    }

    @RequestMapping("adminCheck")
    @ResponseBody
    public String adminCheck(){
        return "角色认证成功";
    }
}
