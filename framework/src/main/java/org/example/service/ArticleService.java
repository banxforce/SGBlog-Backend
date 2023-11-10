package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddArticleDto;
import org.example.domain.dto.ArticleListDto;
import org.example.domain.dto.UpdateArticleDto;
import org.example.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult<Object> hotArticleList();

    ResponseResult<Object> articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult<Object> articleDetails(Long id);

    ResponseResult<Object> updateViewCount(Long id);

    ResponseResult<Object> addArticle(AddArticleDto addArticleDto);

    ResponseResult<Object> adminArticleList(Long pageNum, Long pageSize, ArticleListDto articleListDto);

    ResponseResult<Object> getArticleById(Long id);

    ResponseResult<Object> updateArticle(UpdateArticleDto updateArticleDto);

    ResponseResult<Object> deleteArticle(Long id);
}
