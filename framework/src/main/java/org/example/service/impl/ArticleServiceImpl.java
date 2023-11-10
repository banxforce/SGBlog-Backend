package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.impl.Base64UrlCodec;
import jakarta.annotation.Resource;
import org.example.constant.RedisKeyConstants;
import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddArticleDto;
import org.example.domain.dto.ArticleListDto;
import org.example.domain.dto.UpdateArticleDto;
import org.example.domain.entity.Article;
import org.example.domain.entity.ArticleTag;
import org.example.domain.entity.Category;
import org.example.domain.entity.Tag;
import org.example.domain.vo.*;
import org.example.mapper.ArticleMapper;
import org.example.mapper.ArticleTagMapper;
import org.example.mapper.CategoryMapper;
import org.example.service.ArticleService;
import org.example.service.ArticleTagService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.RedisCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.events.Event;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CategoryMapper categoryMapper; // 引用mapper，避免引用service从而出现循环依赖的问题
    @Resource
    private ArticleTagMapper articleTagMapper;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult<Object> hotArticleList() {
        //查询热门文章,封装成ResponseResult返回
        //封装成lambda可以使用方法引用,避免字段名打错的情况
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                //按访问量降序排序
                .orderByDesc(Article::getViewCount);
        //最多查询10条
        Page<Article> articles = articleMapper.selectPage(Page.of(SystemConstants.HOT_ARTICLE_PAGE_NUMBER, SystemConstants.HOT_ARTICLE_PAGE_SIZE), queryWrapper);
        List<Article> records = articles.getRecords();


        //VO优化,Bean拷贝
 /*
        List<HotArticleVo> hotArticleVos = new ArrayList<>();
        records.forEach(record -> {
            HotArticleVo vo = new HotArticleVo();
            BeanUtils.copyProperties(record,vo);
            hotArticleVos.add(vo);
        });
*/
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult<Object> articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            //如果有categoryId,就要查询与之匹配的内容
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId)
            //状态是正式发布的
                .eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL)
            //对isTop降序排序
                .orderByDesc(Article::getIsTop);

        //分页查询文章表
        Page<Article> page = Page.of(pageNum, pageSize);
        List<Article> articles = articleMapper.selectPage(page, queryWrapper).getRecords();
        //通过categoryId查找category实体，设置categoryName
        /*
        for (Article article : articles) {
            Category category = categoryMapper.selectById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }
        */
        //stream流基于引用，所以map改变了原集合，最后的toList()触发最终计算
        articles.stream()
                .map(article -> article.setCategoryName(categoryMapper.selectById(article.getCategoryId()).getName()))
                .toList();

        //VO优化
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());

        //封装返回
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<Object> articleDetails(Long id) {
        //通过id查文章表
        Article article = articleMapper.selectById(id);
        // 从redis中获取viewCount,redis中存的Integer不能直接转成Long
        Integer viewCount = redisCache.getCacheMapValue(RedisKeyConstants.ARTICLE_VIEW_COUNT, id.toString());
        article.setViewCount(viewCount.longValue());
        //vo优化
        ArticleDetailsVo articleDetailsVo = BeanCopyUtils.copyBean(article, ArticleDetailsVo.class);
        //通过categoryId查找category实体，设置categoryName
        Category category = categoryMapper.selectById(articleDetailsVo.getCategoryId());
        Optional
                .ofNullable(category)
                .ifPresent(category1 -> articleDetailsVo.setCategoryName(category.getName()));
        //封装返回
        return ResponseResult.okResult(articleDetailsVo);
    }

    @Override
    public ResponseResult<Object> updateViewCount(Long id) {
        // 根据id更新redis中的阅读量
        redisCache.incrementCacheMapValue(RedisKeyConstants.ARTICLE_VIEW_COUNT,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<Object> addArticle(AddArticleDto addArticleDto) {
        // 封装成Article
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        // 存入表,注意公共字段,执行操作后，article会被更新为记录的映射
        save(article);
        // 根据 tags字段在sg_article_tag表中建立关系
        Long articleId = article.getId();
        List<ArticleTag> articleTagList = addArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleId, tagId))
                .toList();
        articleTagService.saveBatch(articleTagList);
        // 返回
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Object> adminArticleList(Long pageNum, Long pageSize, ArticleListDto articleListDto) {
        // 根据标题和摘要模糊查询
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(articleListDto.getTitle()),Article::getTitle,articleListDto.getTitle())
                .like(StringUtils.hasText(articleListDto.getContent()),Article::getContent,articleListDto.getContent());
        // 分页查询未被删除的文章。配置文件中已经设置了逻辑删除字段
        Page<Article> page = page(Page.of(pageNum, pageSize), queryWrapper);
        // 封装
        List<AdminArticleListVo> adminArticleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), AdminArticleListVo.class);
        PageVo result = new PageVo(adminArticleListVos, page.getTotal());
        // 返回
        return ResponseResult.okResult(result);
    }

    @Override
    public ResponseResult<Object> getArticleById(Long id) {
        // 根据id查询文章信息
        Article article = getById(id);
        // 封装
        AdminUpdateArticleVo vo = BeanCopyUtils.copyBean(article, AdminUpdateArticleVo.class);
        // 设置tags字段
            //通过文章id到表article_tag中得到tagId集合
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId,vo.getId());
        List<Long> tags = articleTagService.list(queryWrapper).stream()
                .map(ArticleTag::getTagId)
                .toList();

        vo.setTags(tags);
        // 返回
        return ResponseResult.okResult(vo);
    }

    @Transactional
    @Override
    public ResponseResult<Object> updateArticle(UpdateArticleDto updateArticleDto) {
        // 封装成实体类
        Article article = BeanCopyUtils.copyBean(updateArticleDto, Article.class);
        // 更新article表记录以及article_tag表记录
        updateById(article);
        Long articleId = article.getId();
        // 先删除再保存,ArticleTag没设置逻辑主键
        articleTagService.remove(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId,articleId));
        updateArticleDto.getTags()
                .forEach(tag -> articleTagService.save(new ArticleTag(articleId,tag)));

        // 封装vo
        AdminUpdateArticleVo vo = BeanCopyUtils.copyBean(updateArticleDto, AdminUpdateArticleVo.class);
        // 返回
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult<Object> deleteArticle(Long id) {
        // 逻辑删除记录
        removeById(id);
        // 删除article_tag中的有关记录
        articleTagService.remove(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, id));
        // 返回
        return ResponseResult.okResult();
    }

}
