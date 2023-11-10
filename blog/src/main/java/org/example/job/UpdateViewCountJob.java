package org.example.job;

import jakarta.annotation.Resource;
import org.example.constant.RedisKeyConstants;
import org.example.domain.entity.Article;
import org.example.mapper.ArticleMapper;
import org.example.service.ArticleService;
import org.example.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class UpdateViewCountJob {

    @Resource
    private RedisCache redisCache;
    @Resource
    private ArticleService articleService;

    @Scheduled(cron = "0/10 * * * * *")
    public void updateViewCount(){
        // 从redis中读取范文量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(RedisKeyConstants.ARTICLE_VIEW_COUNT);
        // 存入数据表中
            // 先转换成实体类集合再调用service中的批处理
        List<Article> articles = viewCountMap.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .toList();
        System.out.println(articles);
        articleService.updateBatchById(articles);
    }

}
