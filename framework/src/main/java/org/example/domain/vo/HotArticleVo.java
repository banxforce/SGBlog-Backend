package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticleVo {

    private Long id;
    private String title;//标题
    private Long viewCount;//访问量

}
