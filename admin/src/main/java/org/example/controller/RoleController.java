package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.domain.dto.RoleListDto;
import org.example.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system/role")
@Tag(name = "角色")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Operation(description = "查询文章列表")
    @SystemLog(businessName = "查询文章列表")
    @PreAuthorize("@permissionService.hasPermission('system:role:list')")
    @GetMapping("/list")
    public ResponseResult<Object> list(Long pageNum, Long pageSize, RoleListDto roleListDto){
        return roleService.list(pageNum,pageSize,roleListDto);
    }
}
