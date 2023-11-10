package org.example.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.domain.entity.LoginUser;
import org.example.domain.entity.UserRole;
import org.example.domain.vo.UserInfoVo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
public class SecurityUtils {

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        Object principal = getAuthentication().getPrincipal();
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        return id != null && 1L == id;
    }

    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
}