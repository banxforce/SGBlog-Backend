package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddUserDto;
import org.example.domain.dto.GetUserListDto;
import org.example.domain.dto.UpdateUserDto;
import org.example.domain.entity.Role;
import org.example.domain.entity.User;
import org.example.domain.entity.UserRole;
import org.example.domain.vo.*;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.example.mapper.RoleMapper;
import org.example.mapper.UserMapper;
import org.example.mapper.UserRoleMapper;
import org.example.service.UserService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private PasswordEncoder passwordEncoder;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;

    @Autowired
    public UserServiceImpl(UserRoleMapper userRoleMapper, RoleMapper roleMapper) {
        this.userRoleMapper = userRoleMapper;
        this.roleMapper = roleMapper;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseResult<Object> userinfo() {
        // 获取用户id
        Long userId = SecurityUtils.getUserId();
        // 查询用户信息
        User user = this.getById(userId);
        // 封装返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult<Object> updateUserInfo(UserInfoVo userInfoVo) {
        // UserInfoVo转化成User
        User user = BeanCopyUtils.copyBean(userInfoVo, User.class);
        //存入数据库
        updateById(user);
        //返回
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Object> register(User user) {
        // 判断有无空字段，以及唯一信息是否重复
        this.userInfoNotNullAndNotExist(user);
        // 判断密码格式是否正确
        String password = user.getPassword();
        if(!validatePassword(password)){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_FORMAT_ERROR);
        }
        // 对密码进行加密
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
        // 存入数据表
        save(user);
        // 封装返回
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Object> getPageList(Long pageNum, Long pageSize, GetUserListDto userDto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 需要用户分页列表接口。
        Page<User> page = Page.of(pageNum, pageSize);
        // 可以根据用户名模糊搜索。
        queryWrapper.like(StringUtils.hasText(userDto.getUserName()),
                User::getUserName,
                userDto.getUserName());
        // 可以进行手机号的搜索。
        queryWrapper.eq(StringUtils.hasText(userDto.getPhonenumber()),
                User::getPhonenumber,
                userDto.getPhonenumber());
        // 可以进行状态的查询。
        queryWrapper.eq(StringUtils.hasText(userDto.getStatus()),
                User::getStatus,
                userDto.getStatus());
        // 转换成vo
        Page<User> userPage = this.page(page, queryWrapper);
        List<GetUserListVo> userListVos = BeanCopyUtils.copyBeanList(userPage.getRecords(), GetUserListVo.class);

        return new ResponseResult<>().ok( new PageVo(userListVos,userPage.getTotal()) );
    }

    @Transactional
    @Override
    public ResponseResult<Object> addUser(AddUserDto addUserDto) {
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        // 判断有无空字段，以及唯一信息是否重复
        this.userInfoNotNullAndNotExist(user);
        // 判断手机号是否为空
        if(!StringUtils.hasText(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_NOT_NULL);
        }
        // 判断手机号是否已存在
        if(this.phoneNumberExist(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        // 判断密码格式是否正确
        String password = user.getPassword();
        if(!validatePassword(password)){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_FORMAT_ERROR);
        }
        // 对密码进行加密
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
        // 存入数据表
        this.save(user);
        // 保存对应的role信息;需要放在save后，不然user内没有id
        Long userId = user.getId();
        addUserDto.getRoleIds()
                .forEach(roleId -> this.userRoleMapper.insertByUserId(new UserRole(userId,roleId)));
        // 封装返回
        return ResponseResult.okResult();

    }

    @Transactional
    @Override
    public ResponseResult<Object> deleteUser(List<Long> ids) {
        // 遍历id集合
        ids.forEach(id -> {
            // 删除user_role表中的相关记录
            LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserRole::getUserId,id);
            userRoleMapper.delete(queryWrapper);
            // 删除user
            this.removeById(id);
        });
        // 返回
        return new ResponseResult<>();
    }

    @Override
    public ResponseResult<Object> getUserInfoById(Long id) {
        // roleIds：用户所关联的角色id列表
        List<Long> roleIds = this.userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, id))
                .stream()
                .map(UserRole::getRoleId)
                .toList();
        // roles：所有角色的列表
        List<Role> roles = new ArrayList<>();
        if(!roleIds.isEmpty()){
            roles = this.roleMapper.selectBatchIds(roleIds);
        }
        // user：用户信息
        User user = getById(id);
        // 转换成VO
        List<GetAllRoleVo> roleVos = BeanCopyUtils.copyBeanList(roles, GetAllRoleVo.class);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        // 返回
        return new ResponseResult<>().ok( new SystemGetUserByIdVo(roleIds,roleVos,userInfoVo) );
    }

    @Transactional
    @Override
    public ResponseResult<Object> updateUser(UpdateUserDto updateUserDto) {
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        // 判断昵称是否为空
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        // 存入数据表
        this.updateById(user);
        // 更新对应的role信息;需要放在save后，不然user内没有id
        Long userId = user.getId();
        List<Long> roleIds = updateUserDto.getRoleIds();
        this.userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId,userId));
        roleIds.forEach(roleId ->
                this.userRoleMapper.insertByUserId(new UserRole(userId,roleId))
        );
        // 封装返回
        return ResponseResult.okResult();
    }

    private void userInfoNotNullAndNotExist(User user){
        // 判断信息是否为空
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }

        // 判断信息是否已存在
        if(usernameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nicknameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        return count(queryWrapper)>0;
    }

    private boolean nicknameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper)>0;
    }

    private boolean usernameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }
    private boolean phoneNumberExist(String phoneNumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber,phoneNumber);
        return count(queryWrapper)>0;
    }

    private static boolean validatePassword(String password) {
        // 正则表达式：只包含字母、数字和下划线,6-12位
        String regex = "^[a-zA-Z0-9_]{6,12}$";
        return password.matches(regex);
    }

}
