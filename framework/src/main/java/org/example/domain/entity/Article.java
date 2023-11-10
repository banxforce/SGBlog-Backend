package org.example.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文章表(SgArticle)表实体类
 *
 * @author makejava
 * @since 2023-09-20 16:07:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sg_article")
@Accessors(chain = true)
public class Article {

    @TableId
    private Long id;

    private String title;//标题

    private String content;//文章内容

    private String summary;//文章摘要

    private Long categoryId;//所属分类id

    @TableField(exist = false)
    private String categoryName;//分类名

    private String thumbnail;//缩略图

    private String isTop;//是否置顶（0否，1是）

    private String status;//状态（0已发布，1草稿）

    private Long viewCount;//访问量

    private String isComment;//是否允许评论 1是，0否

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Integer delFlag;//删除标志（0代表未删除，1代表已删除）

    public Article(Long id, Long viewCount) {
        this.id = id;
        this.viewCount = viewCount;
    }
}

