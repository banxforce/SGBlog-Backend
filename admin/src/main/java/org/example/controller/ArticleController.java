package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddArticleDto;
import org.example.domain.dto.ArticleListDto;
import org.example.domain.dto.UpdateArticleDto;
import org.example.service.ArticleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "文章相关")
@RequestMapping("/content/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PreAuthorize("@permissionService.hasPermission('content:article:writer')")
    @Operation(description = "新增博文")
    @SystemLog(businessName = "新增博文")
    @PostMapping
    public ResponseResult<Object> addArticle(@RequestBody AddArticleDto addArticleDto){
        return articleService.addArticle(addArticleDto);
    }

    @PreAuthorize("@permissionService.hasPermission('content:article:list')")
    @Operation(description = "查询文章列表")
    @SystemLog(businessName = "查询文章列表")
    @GetMapping("/list")
    public ResponseResult<Object> articleList(Long pageNum, Long pageSize, ArticleListDto articleListDto){
        return articleService.adminArticleList(pageNum,pageSize,articleListDto);
    }

    //@PreAuthorize("@permissionService.hasPermission('content:article:writer')")
    @Operation(description = "按Id查询文章详细信息")
    @SystemLog(businessName = "按Id查询文章详细信息")
    @GetMapping("/{id}")
    public ResponseResult<Object> getArticleById(@PathVariable Long id){
        return articleService.getArticleById(id);
    }

    //@PreAuthorize("@permissionService.hasPermission('content:article:writer')")
    @Operation(description = "更新文章信息")
    @SystemLog(businessName = "更新文章信息")
    @PutMapping
    public ResponseResult<Object> updateArticle(@RequestBody UpdateArticleDto updateArticleDto){
        return articleService.updateArticle(updateArticleDto);
    }

    @Operation(description = "逻辑删除指定文章")
    @SystemLog(businessName = "逻辑删除指定文章")
    @DeleteMapping("/{id}")
    public ResponseResult<Object> deleteArticle(@PathVariable Long id){
        return articleService.deleteArticle(id);
    }

}
