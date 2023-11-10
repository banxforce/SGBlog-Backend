package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.domain.dto.LoginUserDto;
import org.example.domain.entity.LoginUser;
import org.example.domain.entity.Role;
import org.example.domain.entity.User;
import org.example.domain.entity.UserRole;
import org.example.domain.vo.AdminUserInfoVo;
import org.example.domain.vo.MenuVo;
import org.example.domain.vo.RoutersVo;
import org.example.domain.vo.UserInfoVo;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.example.service.*;
import org.example.utils.BeanCopyUtils;
import org.example.utils.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "后台登录")
public class LoginController {

    @Resource
    private LoginService loginService;
    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;

    @Operation(description = "登录")
    @SystemLog(businessName = "登录")
    @PostMapping("/user/login")
    public ResponseResult<Object> login(@RequestBody LoginUserDto loginUserDto){
        if(!StringUtils.hasText(loginUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        User user = BeanCopyUtils.copyBean(loginUserDto, User.class);
        return loginService.login(user);
    }

    @Operation(description = "退出登录")
    @SystemLog(businessName = "登出")
    @PostMapping("/user/logout")
    public ResponseResult<Object> logout(){
        // 请求头中的携带token,可以通过@RequestHeader("token")获取,但没必要
        return loginService.logout();
    }


    @Operation(description = "获取后台用户信息")
    @SystemLog(businessName = "获取后台用户信息")
    @GetMapping("/getInfo")
    public ResponseResult<Object> getInfo(){
        // 获取当前用户及Id
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = loginUser.getUser().getId();
        // 根据用户id获取permissions集合
        List<String> perms = menuService.selectPermsByUserId(userId);
        // 根据用户id获取roles集合
        List<String> roleKeys = roleService.selectRoleKeysByUserId(userId);
        // 获得userInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        // 封装
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeys, userInfoVo);
        //返回
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @Operation(description = "为前端动态路由提供数据")
    @SystemLog(businessName = "为前端动态路由提供数据")
    @GetMapping("/getRouters")
    public ResponseResult<Object> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<MenuVo> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

}
