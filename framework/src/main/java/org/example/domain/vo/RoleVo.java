package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleVo{
        private Long id;//角色ID

        private String roleName;//角色名称

        private String roleKey;//角色权限字符串

        private Integer roleSort;//显示顺序

        private String status;//角色状态（0正常 1停用）{
}
