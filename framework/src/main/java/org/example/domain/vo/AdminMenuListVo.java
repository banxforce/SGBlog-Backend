package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMenuListVo {

    private Long id;//菜单ID

    private String menuName;//菜单名称

    private Long parentId;//父菜单ID

    private Integer orderNum;//显示顺序

    private String path;//路由地址

    private String component;//组件路径

    private Integer isFrame;//是否为外链（0是 1否）

    private String menuType;//菜单类型（M目录 C菜单 F按钮）

    private String visible;//菜单状态（0显示 1隐藏）

    private String status;//菜单状态（0正常 1停用）

    private String perms;//权限标识

    private String icon;//菜单图标

    private String remark;//备注


}
