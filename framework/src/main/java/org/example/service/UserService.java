package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.entity.User;
import org.example.domain.vo.UserInfoVo;

public interface UserService extends IService<User> {

    ResponseResult<Object> userinfo();

    ResponseResult<Object> updateUserInfo(UserInfoVo userInfoVo);

    ResponseResult<Object> register(User user);
}
