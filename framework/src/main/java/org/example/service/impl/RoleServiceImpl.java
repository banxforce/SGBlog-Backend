package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.ResponseResult;
import org.example.domain.dto.RoleListDto;
import org.example.domain.entity.LoginUser;
import org.example.domain.entity.Role;
import org.example.domain.entity.UserRole;
import org.example.domain.vo.PageVo;
import org.example.domain.vo.RoleVo;
import org.example.domain.vo.UserInfoVo;
import org.example.mapper.RoleMapper;
import org.example.service.RoleService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public ResponseResult<Object> list(Long pageNum, Long pageSize, RoleListDto roleListDto) {
        // 条件查询
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                // 可选按名称角色名称进行模糊查询
                .like(StringUtils.hasText(roleListDto.getRoleName()),Role::getRoleName,roleListDto.getRoleName())
                // 可选针对状态查询
                .like(StringUtils.hasText(roleListDto.getStatus()),Role::getStatus,roleListDto.getStatus())
                // 按照role_sort进行升序排列
                .orderByAsc(Role::getRoleSort);
        // 分页查询
        Page<Role> page = this.page(Page.of(pageNum, pageSize), queryWrapper);
        // 封装返回
        PageVo pageVo = new PageVo(BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

}

