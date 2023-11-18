package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddMenuDto;
import org.example.domain.dto.MenulListDto;
import org.example.domain.dto.UpdateMenuDto;
import org.example.domain.entity.Menu;
import org.example.domain.vo.MenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-10-17 16:26:25
 */
public interface MenuService extends IService<Menu> {

    public List<String> selectPermsByUserId(Long userId);

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult<Object> getMenuList(MenulListDto menulListDto);

    ResponseResult<Object> addMenu(AddMenuDto addMenuDto);

    ResponseResult<Object> getMenuById(Long id);

    ResponseResult<Object> updateMenu(UpdateMenuDto menuDto);

    ResponseResult<Object> removeMenu(Long id);

    ResponseResult<Object> treeSelect();

    ResponseResult<Object> roleMenuTreeSelect(Long id);
}

