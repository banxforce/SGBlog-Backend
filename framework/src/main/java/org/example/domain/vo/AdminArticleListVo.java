package org.example.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminArticleListVo {

    private Long id;

    private String title;//标题

    private String content;//文章内容

    private String summary;//文章摘要

    private Long categoryId;//所属分类id

    private String thumbnail;//缩略图

    private String isTop;//是否置顶（0否，1是）

    private String status;//状态（0已发布，1草稿）

    private Long viewCount;//访问量

    private String isComment;//是否允许评论 1是，0否

    private Date createTime;


}
