package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddUserDto;
import org.example.domain.dto.GetUserListDto;
import org.example.domain.dto.UpdateUserDto;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("system/user")
@Tag(name = "用户")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(description = "获取用户列表")
    @SystemLog(businessName = "获取用户列表")
    @PreAuthorize("@permissionService.hasPermission('system:user:list')")
    @GetMapping("/list")
    public ResponseResult<Object> getList(Long pageNum, Long pageSize, GetUserListDto userDto){
        return userService.getPageList(pageNum,pageSize,userDto);
    }

    @Operation(description = "新增用户")
    @SystemLog(businessName = "新增用户")
    @PreAuthorize("@permissionService.hasPermission('system:user:add')")
    @PostMapping
    public ResponseResult<Object> addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @Operation(description = "删除用户")
    @SystemLog(businessName = "删除用户")
    @PreAuthorize("@permissionService.hasPermission('system:user:system:user:remove')")
    @DeleteMapping("/{id}")
    public ResponseResult<Object> deleteUser(@PathVariable Long ...id){
        List<Long> ids = new ArrayList<>(Arrays.asList(id));
        return userService.deleteUser(ids);
    }

    @Operation(description = "根据id查询用户信息回显接口")
    @SystemLog(businessName = "根据id查询用户信息回显接口")
    @PreAuthorize("@permissionService.hasPermission('system:user:query')")
    @GetMapping("/{id}")
    public ResponseResult<Object> getUserInfoById(@PathVariable Long id){
        return userService.getUserInfoById(id);
    }

    @Operation(description = "更新用户信息接口")
    @SystemLog(businessName = "更新用户信息接口")
    @PreAuthorize("@permissionService.hasPermission('system:user:edit')")
    @PutMapping
    public ResponseResult<Object> updateUser(@RequestBody UpdateUserDto updateUserDto){

        return userService.updateUser(updateUserDto);
    }

}
