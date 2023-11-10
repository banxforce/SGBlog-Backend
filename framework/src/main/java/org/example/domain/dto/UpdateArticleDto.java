package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "更新文章信息Dto")
public class UpdateArticleDto {

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

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    private Integer delFlag;//删除标志（0代表未删除，1代表已删除）

    private List<Long> tags; // 标签id集合

}