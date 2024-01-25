package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddCategoryDto;
import org.example.domain.dto.PageSelectDto;
import org.example.domain.dto.UpdateCategoryDto;
import org.example.domain.entity.Category;

import java.io.Serializable;
import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-09-23 13:10:24
 */
public interface CategoryService extends IService<Category> {

    ResponseResult<Object> getCategoryList();

    ResponseResult<Object> listAllCategory();

    void export(HttpServletResponse response);

    ResponseResult<Object> pageList(PageSelectDto pageSelectDto);

    ResponseResult<Object> addCategory(AddCategoryDto addCategoryDto);

    ResponseResult<Object> getCategoryById(Serializable id);

    ResponseResult<Object> updateCategory(UpdateCategoryDto categoryDto);

    ResponseResult<Object> deleteById(List<Long> ids);
}

