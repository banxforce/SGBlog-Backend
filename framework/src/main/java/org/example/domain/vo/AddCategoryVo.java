package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryVo {

    private Long id;
    private String name;//分类名
    private String description;//描述
    private String status;//状态0:正常,1禁用

}
