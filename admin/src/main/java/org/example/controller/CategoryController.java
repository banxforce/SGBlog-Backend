package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "分类")
@RequestMapping("/content/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;


    //@PreAuthorize("@permissionService.hasPermission('content:category:list')")
    @Operation(description = "查询所有分类")
    @SystemLog(businessName = "查询所有分类")
    @GetMapping("/listAllCategory")
    public ResponseResult<Object> listAllCategory(){
        return categoryService.listAllCategory();
    }

    @PreAuthorize("@permissionService.hasPermission('content:category:export')")
    @Operation(description = "导出所有分类到Excel")
    // 日志会调用HttpServletResponse的输出流，导致注入报错
    //@SystemLog(businessName = "导出所有分类到Excel")
    @GetMapping("/export")
    public void export(@Autowired HttpServletResponse response){
        categoryService.export(response);
    }

}
