package org.example.service;

import org.example.domain.ResponseResult;
import org.example.domain.entity.User;

public interface BlogLoginService {
    ResponseResult<Object> login(User user);

    ResponseResult<Object> logout();
}
