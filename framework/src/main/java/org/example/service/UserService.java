package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddUserDto;
import org.example.domain.dto.GetUserListDto;
import org.example.domain.dto.UpdateUserDto;
import org.example.domain.entity.User;
import org.example.domain.vo.UserInfoVo;

import java.util.List;

public interface UserService extends IService<User> {

    ResponseResult<Object> userinfo();

    ResponseResult<Object> updateUserInfo(UserInfoVo userInfoVo);

    ResponseResult<Object> register(User user);

    ResponseResult<Object> getPageList(Long pageNum, Long pageSize, GetUserListDto userDto);

    ResponseResult<Object> addUser(AddUserDto addUserDto);

    ResponseResult<Object> deleteUser(List<Long> ids);

    ResponseResult<Object> getUserInfoById(Long id);

    ResponseResult<Object> updateUser(UpdateUserDto updateUserDto);
}
