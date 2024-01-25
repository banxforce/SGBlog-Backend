package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "更新分类Dto")
public class UpdateCategoryDto {

    private Long id;
    private String name; //分类名
    private String description; //描述
    private String status;//状态0:正常,1禁用

}
