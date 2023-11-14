package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色列表Dto")
public class RoleListDto {
    private String roleName;//角色名称
    private String status;//角色状态（0正常 1停用）
}
