package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "新增角色Dto")
public class UpdateRoleDto {

    private Long id;//角色ID

    private String roleName;//角色名称

    private String roleKey;//角色权限字符串

    private Integer roleSort;//显示顺序

    private String status;//角色状态（0正常 1停用）

    private List<Long> menuIds;
}
