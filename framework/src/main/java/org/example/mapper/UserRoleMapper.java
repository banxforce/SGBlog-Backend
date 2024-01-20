package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.domain.entity.UserRole;


/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-17 12:48:20
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    int insertByUserId(@Param("userRole") UserRole userRole);
}

