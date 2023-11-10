package org.example.service.impl;

import jakarta.annotation.Resource;
import org.example.constant.RedisKeyConstants;
import org.example.domain.ResponseResult;
import org.example.domain.entity.LoginUser;
import org.example.domain.entity.User;
import org.example.domain.vo.BlogUserLoginVo;
import org.example.domain.vo.UserInfoVo;
import org.example.service.BlogLoginService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.JwtUtils;
import org.example.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTMLEditorKit;
import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    /*
    间接注入,security中定义的接口,由子模块中实现了
     */
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;

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
        redisCache.setCacheObject(RedisKeyConstants.BLOGLOGIN_PREFIX + userId,loginUser);
        //把User转化成UserinfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        //把token和userinfo封装
        BlogUserLoginVo vo = new BlogUserLoginVo(token, userInfoVo);
        //封装返回
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult<Object> logout() {
        //通过SecurityContext拿到loginUser
            //在自定义的jwt过滤器中,为合法的请求设置了Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // 我们放入的Principal是loginUser实体类。默认过滤器放入的也是实体类
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userId
        String userId = loginUser.getUser().getId().toString();
        //从redis中删除信息
        redisCache.deleteObject(RedisKeyConstants.BLOGLOGIN_PREFIX + userId);

        return ResponseResult.okResult();
    }


}
