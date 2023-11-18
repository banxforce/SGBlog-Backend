package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddRoleDto;
import org.example.domain.dto.RoleChangeStatusDto;
import org.example.domain.dto.RoleListDto;
import org.example.domain.dto.UpdateRoleDto;
import org.example.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-10-17 13:00:40
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeysByUserId(Long userId);

    ResponseResult<Object> list(Long pageNum, Long pageSize, RoleListDto roleListDto);

    ResponseResult<Object> changeStatus(RoleChangeStatusDto dto);

    ResponseResult<Object> addRole(AddRoleDto dto);

    ResponseResult<Object> getRoleById(Long id);

    ResponseResult<Object> updateRole(UpdateRoleDto updateRoleDto);

    ResponseResult<Object> deleteRole(Long id);
}

