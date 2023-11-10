package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListVo {

    private Long id;

    private String title;//标题

    private String summary;//文章摘要

    private String categoryName;//所属分类名

    private String thumbnail;//缩略图

    private Long viewCount;//访问量

    private Date createTime;
}
