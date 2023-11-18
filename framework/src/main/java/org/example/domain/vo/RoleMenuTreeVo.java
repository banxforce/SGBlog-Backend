package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuTreeVo {

    private List<AdminMenuTreeVo> menus; //菜单树

    private List<Long> checkedKeys; // 角色所关联的菜单权限id列表。
}
