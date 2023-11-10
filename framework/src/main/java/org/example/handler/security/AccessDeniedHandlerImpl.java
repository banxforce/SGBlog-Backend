package org.example.handler.security;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.domain.ResponseResult;
import org.example.enums.AppHttpCodeEnum;
import org.example.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        //便于定位
        accessDeniedException.printStackTrace();
        ResponseResult<Object> result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATION_AUTH);
        //响应给前端
        WebUtils.renderString(response,JSON.toJSONString(result));
    }
}
