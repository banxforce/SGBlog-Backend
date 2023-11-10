package org.example.service.impl;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.example.constant.RedisKeyConstants;
import org.example.domain.ResponseResult;
import org.example.domain.entity.LoginUser;
import org.example.domain.entity.User;
import org.example.domain.vo.UserInfoVo;
import org.example.mapper.UserRoleMapper;
import org.example.service.LoginService;
import org.example.service.UserRoleService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.JwtUtils;
import org.example.utils.RedisCache;
import org.example.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public ResponseResult<Object> login(User user) {
        //判断是否通过
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //获取userId,生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtils.creatJwt(userId);
        //将LoginUser存入redis,key:前缀 + userId
        redisCache.setCacheObject(RedisKeyConstants.LOGIN_PREFIX + userId,loginUser);
        //封装返回
        JSONObject result = new JSONObject();
        result.put("token",token);
        return ResponseResult.okResult(result);
    }

    @Override
    public ResponseResult<Object> logout() {
        //获取当前登录用户的userId
        Long userId = SecurityUtils.getUserId();
        //从redis中删除信息
        redisCache.deleteObject(RedisKeyConstants.LOGIN_PREFIX + userId);

        return ResponseResult.okResult();
    }

}
