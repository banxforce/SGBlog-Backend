package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "新增角色Dto")
public class AddRoleDto {

    private String roleName;//角色名称

    private String roleKey;//角色权限字符串

    private Integer roleSort;//显示顺序

    private String status;//角色状态（0正常 1停用）

    private List<Long> menuIds;

    private String remark;//备注

}
