package org.example.service;

import org.example.utils.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    /**
     * 判断用户是否具有permission
     * @param permission 要判断的权限
     * @return true：具有，false：不具有
     */
    public boolean hasPermission(String permission){
        if(SecurityUtils.isAdmin()){
            //超级管理员拥有所有权限
            return true;
        }
        // 获取用户的权限集合
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        // 查看是否具有该权限
        return permissions.contains(permission);
    }

}
