package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.domain.entity.User;
import org.example.domain.vo.UserInfoVo;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "UserController",description = "用户操作相关接口")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "获取用户信息",description = "获取当前用户信息")
    @SystemLog(businessName = "用户详情")
    @GetMapping("/userInfo")
    public ResponseResult<Object> userInfo(){
        return userService.userinfo();
    }

    /**
     * @param userInfoVo 防止伪造请求修改敏感字段，例如password
     */
    @Operation(summary = "更新用户信息",description = "更新当前用户信息")
    @Parameters({
            @Parameter(name = "userInfoVo",description = "将传入的参数封装成userInfoVo")
    })
    @ApiResponse(responseCode = "200",description = "ok")
    @SystemLog(businessName = "更新用户信息")
    @PutMapping("/userInfo")
    public  ResponseResult<Object> updateUserInfo(@RequestBody UserInfoVo userInfoVo){
        return userService.updateUserInfo(userInfoVo);
    }

    @Operation(summary = "用户注册",description = "用户注册")
    @SystemLog(businessName = "用户注册")
    @PostMapping("/register")
    public ResponseResult<Object> register(@RequestBody User user){
        return userService.register(user);
    }

}
