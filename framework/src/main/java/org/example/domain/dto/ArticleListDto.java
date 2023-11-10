package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "后台分类查询文章列表Dto")
public class ArticleListDto {

    private String title;//标题

    private String content;//文章内容

}
