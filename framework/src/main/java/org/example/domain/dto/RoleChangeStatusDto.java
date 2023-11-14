package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "修改角色状态Dto")
public class RoleChangeStatusDto {
    private Long id;//角色ID
    private String status;//角色状态（0正常 1停用）
}
