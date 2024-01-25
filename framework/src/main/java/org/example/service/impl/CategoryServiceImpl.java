package org.example.service.impl;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddCategoryDto;
import org.example.domain.dto.PageSelectDto;
import org.example.domain.dto.UpdateCategoryDto;
import org.example.domain.entity.Article;
import org.example.domain.entity.Category;
import org.example.domain.vo.*;
import org.example.enums.AppHttpCodeEnum;
import org.example.mapper.CategoryMapper;
import org.example.service.ArticleService;
import org.example.service.CategoryService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.WebUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-09-23 13:10:24
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private ArticleService articleService;

    @Override
    public ResponseResult<Object> getCategoryList() {
        //查询文章表，状态已发布的文章
        List<Article> articleList = articleService.list(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL));
        //获取文章的类别id，并去重
        Set<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
        //通过类别id查询类别表，要求状态正常
        List<Category> categories = listByIds(categoryIds).stream()
                .filter(category -> SystemConstants.ARTICLE_STATUS_NORMAL.equals(category.getStatus()))
                .toList();
        //vo优化
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        //封装后返回结果
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult<Object> listAllCategory() {
        // 从表中查询状态为正常的category集合
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Category::getStatus,SystemConstants.CATEGORY_STATUS_NORMAL);
        List<Category> list = list(queryWrapper);
        // 封装
        List<ContentCategoryVo> voList = BeanCopyUtils.copyBeanList(list, ContentCategoryVo.class);
        // 返回
        return ResponseResult.okResult(voList);
    }

    @Override
    public void export(HttpServletResponse response) {
        try{
            // 获取category集合
            List<Category> list = list();
            // vo封装
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(list, ExcelCategoryVo.class);
            // 下载的文件名
            String fileName = "分类.xlsx";
            // 设置响应头
            WebUtils.setDownLoadHeader(fileName,response);
            // 输出excel
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("分类")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            // 重置response
            response.reset();
            ResponseResult<Object> result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            // 这里可能有问题
            WebUtils.renderString(response, result);
        }
    }

    @Override
    public ResponseResult<Object> pageList(PageSelectDto pageSelectDto) {
        // 需要分页查询分类列表。
        Page<Category> page = Page.of(pageSelectDto.getPageNum(), pageSelectDto.getPageSize());
        // 能根据分类名称进行模糊查询。
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StringUtils.hasText(pageSelectDto.getName()), Category::getName, pageSelectDto.getName())
                // 能根据状态进行查询。
                .eq(StringUtils.hasText(pageSelectDto.getStatus()), Category::getStatus, pageSelectDto.getStatus());
        Page<Category> result = this.page(page, queryWrapper);
        // 转换成vo
        List<AddCategoryVo> voList = BeanCopyUtils.copyBeanList(result.getRecords(), AddCategoryVo.class);
        // 封装返回
        return new ResponseResult<>().ok(new PageVo(voList, result.getTotal()));
    }

    @Override
    public ResponseResult<Object> addCategory(AddCategoryDto addCategoryDto) {
        // Bean转换
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        // 保存
        this.save(category);
        // 返回
        return new ResponseResult<>();
    }

    @Override
    public ResponseResult<Object> getCategoryById(Serializable id) {
        // 根据id查询
        Category category = this.getById(id);
        // 转换成VO
        AddCategoryVo addCategoryVo = BeanCopyUtils.copyBean(category, AddCategoryVo.class);
        // 返回
        return new ResponseResult<>().ok(addCategoryVo);
    }

    @Override
    public ResponseResult<Object> updateCategory(UpdateCategoryDto categoryDto) {
        // Bean转换
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        // 保存
        this.updateById(category);
        // 返回
        return new ResponseResult<>();
    }

    @Override
    public ResponseResult<Object> deleteById(List<Long> ids) {
        // 根据Id进行逻辑删除
        ids.forEach(this::removeById);
        // 返回
        return new ResponseResult<>();
    }

}

