package org.example.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelCategoryVo {

    @ExcelProperty("分类名")
    private String name;//分类名
    @ExcelProperty("描述")
    private String description;//描述
    @ExcelProperty("状态0:正常,1禁用")
    private String status;//

}
