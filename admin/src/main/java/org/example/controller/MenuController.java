package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddMenuDto;
import org.example.domain.dto.MenulListDto;
import org.example.domain.dto.UpdateMenuDto;
import org.example.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
@Tag(name = "菜单")
public class MenuController {

    @Resource
    private MenuService menuService;

    @PreAuthorize("@permissionService.hasPermission('system:menu:list')")
    @Operation(description = "查菜单列表")
    @SystemLog(businessName = "查菜单列表")
    @GetMapping("/list")
    public ResponseResult<Object> getMenuList(MenulListDto menulListDto){
        return menuService.getMenuList(menulListDto);
    }

    @PreAuthorize("@permissionService.hasPermission('system:menu:add')")
    @Operation(description = "新增菜单")
    @SystemLog(businessName = "新增菜单")
    @PostMapping
    public ResponseResult<Object> addMenu(@RequestBody AddMenuDto addMenuDto){
        return menuService.addMenu(addMenuDto);
    }

    @PreAuthorize("@permissionService.hasPermission('system:menu:query')")
    @Operation(description = "回显菜单")
    @SystemLog(businessName = "回显菜单")
    @GetMapping("/{id}")
    public ResponseResult<Object> getMenuById(@PathVariable Long id){
        return menuService.getMenuById(id);
    }

    @PreAuthorize("@permissionService.hasPermission('system:menu:edit')")
    @Operation(description = "更新菜单")
    @SystemLog(businessName = "更新菜单")
    @PutMapping
    public ResponseResult<Object> updateMenu(@RequestBody UpdateMenuDto updateMenuDto){
        return menuService.updateMenu(updateMenuDto);
    }

    @PreAuthorize("@permissionService.hasPermission('system:menu:remove')")
    @Operation(description = "删除菜单")
    @SystemLog(businessName = "删除菜单")
    @DeleteMapping("/{id}")
    public ResponseResult<Object> removeMenu(@PathVariable Long id){
        return menuService.removeMenu(id);
    }

    @PreAuthorize("@permissionService.hasPermission('system:menu:query')")
    @Operation(description = "获取菜单树")
    @SystemLog(businessName = "获取菜单树")
    @GetMapping("/treeSelect")
    public ResponseResult<Object> treeSelect(){
        return menuService.treeSelect();
    }

    @PreAuthorize("@permissionService.hasPermission('system:menu:query')")
    @Operation(description = "加载对应角色菜单列表树接口")
    @SystemLog(businessName = "加载对应角色菜单列表树接口")
    @GetMapping("/roleMenuTreeSelect/{id}")
    public ResponseResult<Object> roleMenuTreeSelect(@PathVariable Long id){
        return menuService.roleMenuTreeSelect(id);
    }

}
