package org.example.controller;

import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.service.ArticleService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @SystemLog(businessName = "获取热门文章")
    @GetMapping("/hotArticleList")
    public ResponseResult<Object> hotArticleList(){

        return articleService.hotArticleList();
    }

    @SystemLog(businessName = "获取文章列表")
    @GetMapping("/articleList")
    public ResponseResult<Object> articleList(@NonNull Integer pageNum, @NonNull Integer pageSize, Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    @SystemLog(businessName = "查询对于id文章详情")
    @GetMapping("/{id}")
    public ResponseResult<Object> articleDetails(@PathVariable Long id){
        return articleService.articleDetails(id);
    }

    @SystemLog(businessName = "更新访问量")
    @PutMapping("updateViewCount/{id}")
    public ResponseResult<Object> updateViewCount(@PathVariable Long id){
        return articleService.updateViewCount(id);
    }

}
