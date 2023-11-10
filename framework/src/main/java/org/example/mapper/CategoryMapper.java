package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.Category;


/**
 * 分类表(Category)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-23 13:10:24
 */

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}

