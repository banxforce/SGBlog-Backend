package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailsVo {

    private Long id;

    private String title;//标题

    private String content;//文章内容

    private Long categoryId;//分类id

    private String categoryName;//所属分类名

    private String isComment;//是否允许评论 1是，0否

    private Long viewCount;//访问量

    private Date createTime;

}
