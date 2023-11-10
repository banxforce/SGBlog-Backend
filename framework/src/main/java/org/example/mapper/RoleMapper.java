package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-17 13:00:40
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeysByUserId(Long userId);

}

