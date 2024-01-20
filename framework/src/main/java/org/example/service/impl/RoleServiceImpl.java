package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddRoleDto;
import org.example.domain.dto.RoleChangeStatusDto;
import org.example.domain.dto.RoleListDto;
import org.example.domain.dto.UpdateRoleDto;
import org.example.domain.entity.Role;
import org.example.domain.entity.RoleMenu;
import org.example.domain.vo.GetAllRoleVo;
import org.example.domain.vo.PageVo;
import org.example.domain.vo.RoleVo;
import org.example.mapper.RoleMapper;
import org.example.mapper.RoleMenuMapper;
import org.example.service.RoleService;
import org.example.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Resource
    private RoleMenuMapper roleMenuMapper;

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

    @Override
    public ResponseResult<Object> changeStatus(RoleChangeStatusDto dto) {
        //封装
        Role role = BeanCopyUtils.copyBean(dto, Role.class);
        // 更新，注意设置字段的自动填充
        this.updateById(role);
        // 返回
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult<Object> addRole(AddRoleDto dto) {
        // 将dto转化成Role
        Role role = BeanCopyUtils.copyBean(dto, Role.class);
        // 获取menuIds
        List<Long> menuIds = dto.getMenuIds();
        // 插入表记录
        this.save(role);
        // 增加role_menu表记录
        this.addRoleMenu(role.getId(),menuIds);
        // 返回
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Object> getRoleById(Long id) {
        // 查询记录
        Role role = this.getById(id);
        // 封装
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        // 返回
        return ResponseResult.okResult(roleVo);
    }

    @Transactional
    @Override
    public ResponseResult<Object> updateRole(UpdateRoleDto updateRoleDto) {
        // 转化成Role
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        // 获取menuId集合
        List<Long> menuIds = updateRoleDto.getMenuIds();
        // 更新role表
        this.updateById(role);
        // 更新role_menu表
        this.updateRoleMenu(role.getId(),menuIds);
        // 返回
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult<Object> deleteRole(Long id) {
        // 设置了逻辑删除字段，直接调用方法删除即可
        this.removeById(id);
        // 删除role_menu表中对应记录
        this.deleteRoleMenu(id);
        // 返回
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Object> getAllRole() {
        // 查询的是所有状态正常的角色
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<Role>().eq(Role::getStatus, SystemConstants.ROLE_STATUS_NORMAL);
        // 查询
        List<Role> roles = this.list(queryWrapper);
        // 装换成vo
        List<GetAllRoleVo> allRoleVos = BeanCopyUtils.copyBeanList(roles, GetAllRoleVo.class);

        return new ResponseResult<>().ok(allRoleVos);
    }


    private void deleteRoleMenu(Long roleId){
        roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId,roleId));
    }


    private void updateRoleMenu(Long roleId, List<Long> menuIds) {
        // 根据roleId删除RoleMenu中的记录
        this.deleteRoleMenu(roleId);
        // 添加记录
        this.addRoleMenu(roleId,menuIds);
    }

    private void addRoleMenu(Long roleId, List<Long> menuIds) {
        menuIds.stream()
                .map(menuId -> roleMenuMapper.insert(new RoleMenu(roleId,menuId)))
                .toList();
    }

}

