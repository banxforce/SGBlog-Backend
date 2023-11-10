package org.example.controller;

import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @SystemLog(businessName = "获取类别列表")
    @GetMapping("/getCategoryList")
    public ResponseResult<Object> getCategoryList(){
        return categoryService.getCategoryList();
    }

}
