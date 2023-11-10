package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.entity.LoginUser;
import org.example.domain.entity.Role;
import org.example.domain.entity.UserRole;
import org.example.domain.vo.UserInfoVo;
import org.example.mapper.RoleMapper;
import org.example.service.RoleService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-10-17 13:00:40
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> selectRoleKeysByUserId(Long userId) {
        if(userId == 1L){
            return List.of("admin");
        }
        return getBaseMapper().selectRoleKeysByUserId(userId);
    }

}

