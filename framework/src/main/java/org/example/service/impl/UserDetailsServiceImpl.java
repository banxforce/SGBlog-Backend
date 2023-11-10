package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.example.constant.SystemConstants;
import org.example.domain.entity.LoginUser;
import org.example.domain.entity.User;
import org.example.mapper.MenuMapper;
import org.example.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //System.out.println("======\n"+username+"\n=========");
        //根据用户名查询用户
        LambdaQueryWrapper<org.example.domain.entity.User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        //判断用户是否存在
        if(Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //如果类型为后台用户，放入权限信息
        if(SystemConstants.USER_TYPE_ADMIN.equals(user.getType())){
            List<String> perms = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,perms);
        }
        //封装返回
        return new LoginUser(user,null);
    }
}
