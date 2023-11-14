package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.domain.ResponseResult;
import org.example.domain.entity.User;
import org.example.domain.vo.UserInfoVo;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    public ResponseResult<Object> userinfo() {
        // 获取用户id
        Long userId = SecurityUtils.getUserId();
        // 查询用户信息
        User user = this.getById(userId);
        // 封装返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult<Object> updateUserInfo(UserInfoVo userInfoVo) {
        // UserInfoVo转化成User
        User user = BeanCopyUtils.copyBean(userInfoVo, User.class);
        //存入数据库
        updateById(user);
        //返回
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Object> register(User user) {
        // 判断信息是否为空
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        // 判断信息是否已存在
        if(usernameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nicknameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        // 判断密码格式是否正确
        String password = user.getPassword();
        if(!validatePassword(password)){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_FORMAT_ERROR);
        }
        // 对密码进行加密
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
        // 存入数据表
        save(user);
        // 封装返回
        return ResponseResult.okResult();
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        return count(queryWrapper)>0;
    }

    private boolean nicknameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper)>0;
    }

    private boolean usernameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }

    private static boolean validatePassword(String password) {
        // 正则表达式：只包含字母、数字和下划线,6-12位
        String regex = "^[a-zA-Z0-9_]{6,12}$";
        return password.matches(regex);
    }

}
