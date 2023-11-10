package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "修改标签Dto")
public class PutTagDto {

    private Long id;
    private String name;
    private String remark;

}
