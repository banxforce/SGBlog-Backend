package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "更新友链Dto")
public class UpdateLinkDto {

    private Long id;

    private String name;

    private String logo;

    private String description;

    private String address;//网站地址

    private String status;//审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)

}
