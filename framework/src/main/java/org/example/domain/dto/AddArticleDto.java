package org.example.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "新增博文Dto")
public class AddArticleDto {

    private String title;//标题

    private String content;//文章内容

    private String summary;//文章摘要

    private Long categoryId;//所属分类id

    private String thumbnail;//缩略图

    private String isTop;//是否置顶（0否，1是）

    private String status;//状态（0已发布，1草稿）

    private String isComment;//是否允许评论 1是，0否

    private List<Long> tags;//拥有的标签

}
