package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddRoleDto;
import org.example.domain.dto.RoleChangeStatusDto;
import org.example.domain.dto.RoleListDto;
import org.example.domain.dto.UpdateRoleDto;
import org.example.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/role")
@Tag(name = "角色")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Operation(description = "查询角色列表")
    @SystemLog(businessName = "查询角色列表")
    @PreAuthorize("@permissionService.hasPermission('system:role:list')")
    @GetMapping("/list")
    public ResponseResult<Object> list(Long pageNum, Long pageSize, RoleListDto roleListDto){
        return roleService.list(pageNum,pageSize,roleListDto);
    }

    @Operation(description = "改变角色状态")
    @SystemLog(businessName = "改变角色状态")
    @PreAuthorize("@permissionService.hasPermission('system:role:edit')")
    @PutMapping("/changeStatus")
    public ResponseResult<Object> changeStatus(@RequestBody RoleChangeStatusDto dto){
        return roleService.changeStatus(dto);
    }

    @Operation(description = "新增角色")
    @SystemLog(businessName = "新增角色")
    @PreAuthorize("@permissionService.hasPermission('system:role:add')")
    @PostMapping
    public ResponseResult<Object> addRole(@RequestBody AddRoleDto dto){
        return roleService.addRole(dto);
    }

    @Operation(description = "角色信息回显")
    @SystemLog(businessName = "角色信息回显")
    @PreAuthorize("@permissionService.hasPermission('system:role:query')")
    @GetMapping("/{id}")
    public ResponseResult<Object> getRoleById(@PathVariable Long id){
        return roleService.getRoleById(id);
    }

    @Operation(description = "更新角色信息")
    @SystemLog(businessName = "更新角色信息")
    @PreAuthorize("@permissionService.hasPermission('system:role:edit')")
    @PutMapping
    public ResponseResult<Object> updateRole(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRole(updateRoleDto);
    }
    @Operation(description = "删除角色")
    @SystemLog(businessName = "删除角色")
    @PreAuthorize("@permissionService.hasPermission('system:role:remove')")
    @DeleteMapping("/{id}")
    public ResponseResult<Object> deleteRole(@PathVariable Long id){
        return roleService.deleteRole(id);
    }

    @Operation(description = "所有状态正常的角色")
    @SystemLog(businessName = "所有状态正常的角色")
    @PreAuthorize("@permissionService.hasPermission('system:user:add')")
    @GetMapping("/listAllRole")
    public ResponseResult<Object> getAllRole(){
        return roleService.getAllRole();
    }


}
