package org.example.handler.security;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.domain.ResponseResult;
import org.example.enums.AppHttpCodeEnum;
import org.example.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointIml implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        // 便于定位
        authException.printStackTrace();

        ResponseResult<Object> result;
        // InsufficientAuthenticationException BadCredentialsException
        if(authException instanceof BadCredentialsException){
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR,authException.getMessage());
        } else if (authException instanceof InsufficientAuthenticationException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        } else {
            // 提示错误,将问题转嫁给用户
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"认证或授权错误");
        }

        // 响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));

    }

}
