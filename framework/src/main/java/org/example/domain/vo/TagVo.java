package org.example.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagVo {

    private Long id;
    private String name;//标签名
    private String remark;//备注

}
