package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotation.SystemLog;
import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddCategoryDto;
import org.example.domain.dto.PageSelectDto;
import org.example.domain.dto.UpdateCategoryDto;
import org.example.service.CategoryService;
import org.example.utils.DtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "分类")
@RequestMapping("/content/category")
@PreAuthorize("@permissionService.hasPermission('content:category:list')")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

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


    @Operation(description = "分页查询分类列表")
    @SystemLog(businessName = "分页查询分类列表")
    @GetMapping("/list")
    public ResponseResult<Object> pageList(PageSelectDto pageSelectDto){
        return categoryService.pageList(pageSelectDto);
    }

    @Operation(description = "新增分类")
    @SystemLog(businessName = "新增分类")
    @PostMapping
    public ResponseResult<Object> addCategory(@RequestBody AddCategoryDto addCategoryDto){
        // 参数是否非法
        boolean illegal = DtoUtils.hasIllegalParameter(addCategoryDto, AddCategoryDto.class) ||
                !List.of(SystemConstants.CATEGORY_STATUS_NORMAL, SystemConstants.CATEGORY_STATUS_DISABLE).contains(addCategoryDto.getStatus());
        if(illegal) return null;
        return categoryService.addCategory(addCategoryDto);
    }

    @Operation(description = "修改分类-根据id查询分类")
    @SystemLog(businessName = "修改分类-根据id查询分类")
    @GetMapping("/{id}")
    public ResponseResult<Object> getCategoryById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    @Operation(description = "修改分类-更新分类")
    @SystemLog(businessName = "修改分类-更新分类")
    @PutMapping
    public ResponseResult<Object> updateCategory(@RequestBody UpdateCategoryDto categoryDto){
        // 参数是否非法
        boolean illegal = DtoUtils.hasIllegalParameter(categoryDto, UpdateCategoryDto.class) ||
                !List.of(SystemConstants.CATEGORY_STATUS_NORMAL, SystemConstants.CATEGORY_STATUS_DISABLE).contains(categoryDto.getStatus());
        if(illegal) return null;
        return categoryService.updateCategory(categoryDto);
    }

    @Operation(description = "删除分类")
    @SystemLog(businessName = "删除分类")
    @DeleteMapping("/{id}")
    public ResponseResult<Object> deleteById(@PathVariable Long... id){
        return categoryService.deleteById(new ArrayList<>(List.of(id)));
    }

}
