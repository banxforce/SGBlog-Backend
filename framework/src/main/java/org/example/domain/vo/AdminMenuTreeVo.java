package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMenuTreeVo {
    private Long id;//菜单ID
    private List<AdminMenuTreeVo> children;
    private String menuName;//菜单名称
    private Long parentId;//父菜单ID
}
