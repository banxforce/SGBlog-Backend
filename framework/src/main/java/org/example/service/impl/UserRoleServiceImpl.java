package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.entity.UserRole;
import org.example.mapper.UserRoleMapper;
import org.example.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-10-17 12:48:21
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public List<Long> selectRoleIdsByUserId(Long userId) {
        // 根据用户userId到user_role表中获取roleId,再从role表中得到role_key;
        List<Long> roleIds = list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId,userId))
                .stream()
                .map(UserRole::getRoleId)
                .toList();
        return roleIds;
    }



}

