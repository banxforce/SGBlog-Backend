package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文章列表Dto")
public class MenulListDto {

    private String status;//菜单状态（0正常 1停用）
    private String menuName;//菜单名称

}
