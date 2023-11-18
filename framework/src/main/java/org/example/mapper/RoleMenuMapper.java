package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.RoleMenu;


/**
 * 角色和菜单关联表(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-18 17:04:48
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {


}

