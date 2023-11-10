package org.example.service.impl;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Article;
import org.example.domain.entity.Category;
import org.example.domain.vo.CategoryVo;
import org.example.domain.vo.ContentCategoryVo;
import org.example.domain.vo.ExcelCategoryVo;
import org.example.enums.AppHttpCodeEnum;
import org.example.mapper.CategoryMapper;
import org.example.service.ArticleService;
import org.example.service.CategoryService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.WebUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
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
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            // 重置response
            response.reset();
            ResponseResult<Object> result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            //WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

}

