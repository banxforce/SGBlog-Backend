package org.example.runner;

import jakarta.annotation.Resource;
import org.example.constant.RedisKeyConstants;
import org.example.domain.entity.Article;
import org.example.mapper.ArticleMapper;
import org.example.service.ArticleService;
import org.example.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        // 从数据库中读取文章详情
        List<Article> articleList = articleMapper.selectList(null);
        // 封装成 Map<id,viewCount>,并转换类型,类型不能为Long不让会被作为字符串存储:”100L“
        Map<String, Integer> viewCountMap = articleList.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));
        //存入redis
        redisCache.setCacheMap(RedisKeyConstants.ARTICLE_VIEW_COUNT,viewCountMap);
    }
}
