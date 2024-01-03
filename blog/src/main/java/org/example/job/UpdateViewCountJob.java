package org.example.job;

import jakarta.annotation.Resource;
import org.example.constant.RedisKeyConstants;
import org.example.domain.vo.ArticleVIewCountVo;
import org.example.service.ArticleService;
import org.example.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class UpdateViewCountJob {

    @Resource
    private RedisCache redisCache;
    @Resource
    private ArticleService articleService;

    @Scheduled(cron = "0/10 * * * * *")
    public void updateViewCount(){
        // 从redis中读取范文量  Mar<id,viewCount>
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(RedisKeyConstants.ARTICLE_VIEW_COUNT);
        // 存入数据表中
            // 先转换成实体类集合再调用service中的批处理
        List<ArticleVIewCountVo> articles = viewCountMap.entrySet().stream()
                .map(entry -> new ArticleVIewCountVo(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .toList();

        // TODO 更新记录时触发自动填充，未登录时尝试获取userId,抛出异常。解决: 封装一个类，只通过Id更新浏览量
        articleService.updateViewCountBatch(articles);
    }

}
