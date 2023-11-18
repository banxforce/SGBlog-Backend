package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddMenuDto;
import org.example.domain.dto.MenulListDto;
import org.example.domain.dto.UpdateMenuDto;
import org.example.domain.entity.Menu;
import org.example.domain.entity.RoleMenu;
import org.example.domain.vo.*;
import org.example.mapper.MenuMapper;
import org.example.mapper.RoleMenuMapper;
import org.example.service.MenuService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-10-17 16:26:25
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<String> selectPermsByUserId(Long userId) {
        // 如果是超级管理员
        if(SecurityUtils.isAdmin()){
            // 查询所有菜单类型为C或者F的，状态为正常的，未被删除的
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU_TYPE_MENU,SystemConstants.MENU_TYPE_BUTTON);
            queryWrapper.eq(Menu::getStatus,SystemConstants.MENU_STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .toList();
            return perms;
        }
        return menuMapper.selectPermsByUserId(userId);
    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //如果是超级管理员
        if (SecurityUtils.isAdmin()) {
            // 从menu表中获取所有菜单类型为C或者M的，状态为正常的，未被删除的权限记录
            // 查询所有路由菜单
            menus = menuMapper.getAllRouterMenu();
        }
        else {
            // 否则按用户Id获取对应的路由菜单集合
            menus = menuMapper.getRouterMenuByUserId(userId);
        }
        // vo优化
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<MenuVo> menuTree = builderMenuTree(menuVos,SystemConstants.MENU_PARENT);
        return menuTree;
    }

    @Override
    public ResponseResult<Object> getMenuList(MenulListDto menulListDto) {
        // 可以针对菜单名进行模糊查询，也可以针对菜单的状态进行查询,菜单要按照父菜单id和orderNum进行排序
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menulListDto.getMenuName()),Menu::getMenuName,menulListDto.getMenuName())
                .eq(StringUtils.hasText(menulListDto.getStatus()),Menu::getStatus,menulListDto.getStatus())
                .orderByAsc(Menu::getParentId)
                .orderByAsc(Menu::getOrderNum);
        // 查询
        List<Menu> menus = list(queryWrapper);
        // 封装
        List<AdminMenuListVo> vos = BeanCopyUtils.copyBeanList(menus, AdminMenuListVo.class);
        // 返回
        return ResponseResult.okResult(vos);
    }

    @Override
    public ResponseResult<Object> addMenu(AddMenuDto addMenuDto) {
        // 封装
        Menu menu = BeanCopyUtils.copyBean(addMenuDto, Menu.class);
        // 存入表
        save(menu);
        // 返回
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Object> getMenuById(Long id) {
        // 查询
        Menu menu = getById(id);
        // 封装
        AdminMenuVo adminMenuVo = BeanCopyUtils.copyBean(menu, AdminMenuVo.class);
        // 返回
        return ResponseResult.okResult(adminMenuVo);
    }

    @Override
    public ResponseResult<Object> updateMenu(UpdateMenuDto menuDto) {
        // 不能把父菜单设置为当前菜单
        if(menuDto.getParentId().equals(menuDto.getId())){
            throw new RuntimeException("修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        // 封装
        Menu menu = BeanCopyUtils.copyBean(menuDto, Menu.class);
        // 更新表记录
        updateById(menu);
        // 返回
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Object> removeMenu(Long id) {
        // 存在子菜单不允许删除
            // 查询是否存在parentId为此id的记录
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,id);
        if(!list(queryWrapper).isEmpty()){
            throw new RuntimeException("存在子菜单不允许删除");
        }
        // 删除记录
        removeById(id);
        // 返回
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Object> treeSelect() {
        // 获取根菜单集合
        List<Menu> rootMenus = menuMapper.getAllRouterMenu();
        // 转换成menuVo
        List<AdminMenuTreeVo> adminMenuTreeVos = BeanCopyUtils.copyBeanList(rootMenus, AdminMenuTreeVo.class);
        // 构建菜单树
        List<AdminMenuTreeVo> tree = this.builderAdminMenuTree(adminMenuTreeVos, SystemConstants.MENU_PARENT);
        // 封装返回
        return ResponseResult.okResult(tree);
    }

    @Override
    public ResponseResult<Object> roleMenuTreeSelect(Long id) {
        //查询对应RoleId的菜单集合
        List<Menu> menus = this.getMenusByRoleId(id);
        // 转化成VO
        List<AdminMenuTreeVo> adminMenuTreeVos = BeanCopyUtils.copyBeanList(menus, AdminMenuTreeVo.class);
        // 构建菜单树
        List<AdminMenuTreeVo> tree = this.builderAdminMenuTree(adminMenuTreeVos, SystemConstants.MENU_PARENT);
        // 根据roleId 获取对应的角色所关联的菜单权限id列表 menuIds
        List<Long> menuIds = roleMenuMapper.selectList(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, id))
                .stream()
                .map(RoleMenu::getMenuId)
                .toList();
        // 封装返回
        return ResponseResult.okResult(new RoleMenuTreeVo(tree,menuIds));
    }

    private List<Menu> getMenusByRoleId(Long id) {
        List<Long> menuIds = roleMenuMapper.selectList(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, id))
                .stream()
                .map(RoleMenu::getMenuId)
                .toList();
        return this.listByIds(menuIds);
    }

    /**
     * 以parentId为根，menus为节点构建一棵树
     * @param menus 节点集合
     * @param parentId 根节点属性
     * @return 构建好的树形结构
     */
    private List<MenuVo> builderMenuTree(List<MenuVo> menus, Long parentId) {
        List<MenuVo> menuTree = menus.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .peek(menuVo -> menuVo.setChildren(this.getChildren(menuVo, menus)))
                .toList();
        return menuTree;
    }

    /**
     * 根据传入menuVo的parentId为其设置children字段，然后遍历递归设置下一级对象的children字段
     * @param menuVo 要为其设置children字段的对象
     * @param menus  menu集合
     * @return 设置好children字段的对象集合
     */
    private List<MenuVo> getChildren(MenuVo menuVo, List<MenuVo> menus) {
        List<MenuVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menuVo.getId()))
                .peek(m->m.setChildren(this.getChildren(m,menus)))
                .toList();
        return childrenList;
    }

    private List<AdminMenuTreeVo> builderAdminMenuTree(List<AdminMenuTreeVo> menus, Long parentId) {
        List<AdminMenuTreeVo> menuTree = menus.stream()
                .filter(adminMenuTreeVo -> adminMenuTreeVo.getParentId().equals(parentId))
                .peek(adminMenuTreeVo -> adminMenuTreeVo.setChildren(this.getChildren(adminMenuTreeVo, menus)))
                .toList();
        return menuTree;
    }

    private List<AdminMenuTreeVo> getChildren(AdminMenuTreeVo adminMenuTreeVo, List<AdminMenuTreeVo> menus) {
        List<AdminMenuTreeVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(adminMenuTreeVo.getId()))
                .peek(m->m.setChildren(this.getChildren(m,menus)))
                .toList();
        return childrenList;
    }
}

