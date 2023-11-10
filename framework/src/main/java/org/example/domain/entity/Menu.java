package org.example.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 菜单权限表(Menu)表实体类
 *
 * @author makejava
 * @since 2023-10-17 16:26:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_menu")
public class Menu {

    @TableId
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

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;//创建者

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;//创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;//更新者

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;//更新时间

    private String remark;//备注

    private String delFlag;
    

}

