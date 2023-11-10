package org.example.filter;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonObject;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.constant.RedisKeyConstants;
import org.example.domain.ResponseResult;
import org.example.domain.entity.LoginUser;
import org.example.enums.AppHttpCodeEnum;
import org.example.utils.JwtUtils;
import org.example.utils.RedisCache;
import org.example.utils.WebUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;


/*
前后台认证机制不同,所以将这个过滤器定义在前台模块;以拦截的路径区分
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        // 获取token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            //假设该接口不需要登录,直接放行
            filterChain.doFilter(request,response);
            return;
        }
        DecodedJWT verify;
        try {
            verify = JwtUtils.parseJwt(token);
        } catch (Exception e) {
            // token超时,token非法
            //全局异常处理在controller层，filter层在此之前,所以需要处理掉
            ResponseResult<Object> result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        // 解析token,拿到token中的userId
        String userId = verify.getSubject();

//        // 从redis中读取用户信息,返回的是JsonObject,强转会报错
//        Object jsonObject = redisCache.getCacheObject(RedisKeyConstants.BLOGLOGIN_PREFIX + userId);
//        // 将jsonObject转化成String,再转化成目标类
//        LoginUser loginUser = JSON.parseObject(JSON.toJSONString(jsonObject), LoginUser.class);

        LoginUser loginUser = redisCache.getCacheObject(RedisKeyConstants.BLOGLOGIN_PREFIX + userId);

        if(Objects.isNull(loginUser)){
            //说明登录已经过期
            ResponseResult<Object> result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response,JSON.toJSONString(result));
            return;
        }
        // 存入SecurityContextHolder
            /*
              注意: UsernamePasswordAuthenticationToken有两个构造方法,
                两个参数的验证状态为 false
                三个参数的验证状态为 true
              */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //最后放行
        filterChain.doFilter(request,response);
    }

}
