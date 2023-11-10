package org.example.controller;

import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.domain.entity.User;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.example.service.BlogLoginService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Resource
    private BlogLoginService blogLoginService;

    @SystemLog(businessName = "登录")
    @PostMapping("/login")
    public ResponseResult<Object> login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @SystemLog(businessName = "登出")
    @PostMapping("/logout")
    public ResponseResult<Object> logout(){
        // 请求头中的携带token,可以通过@RequestHeader("token")获取,但没必要
        return blogLoginService.logout();
    }

}
