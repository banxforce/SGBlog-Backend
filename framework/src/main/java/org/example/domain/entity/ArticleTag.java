package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author makejava
 * @since 2023-10-23 18:36:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sg_article_tag")
public class ArticleTag {

    private Long articleId;//文章id

    private Long tagId;//标签id
    

}

