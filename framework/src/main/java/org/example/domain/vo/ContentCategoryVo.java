package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentCategoryVo {

    private Long id;
    private String name;//分类名
    private String description;//描述

}
