package com.codexie.controller;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public String unauthorizedException(Exception e){
        return "无权限";
    }

    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public String authorizationException(Exception e){
        return "权限认证失败";
    }

    @ResponseBody
    @ExceptionHandler(ExcessiveAttemptsException.class)
    public String excessiveAttemptsException(Exception ex) {
        return "账户锁定10分钟";
    }
    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public String authenticationException(Exception ex) {
        return "登录失败";
    }

}
