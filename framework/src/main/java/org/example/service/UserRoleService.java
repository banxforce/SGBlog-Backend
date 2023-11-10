package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.UserRole;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {
    public List<Long> selectRoleIdsByUserId(Long userId);

}
