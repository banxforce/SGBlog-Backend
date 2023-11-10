package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.ArticleTag;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-23 18:36:45
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}

