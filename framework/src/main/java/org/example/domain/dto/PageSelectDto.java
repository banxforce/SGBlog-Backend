package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页查询Dto")
public class PageSelectDto {

    private Long pageNum; // 页码

    private Long pageSize;  // 每页条数

    private String name; // 分类名

    private String status; // 状态0:正常,1禁用

}
