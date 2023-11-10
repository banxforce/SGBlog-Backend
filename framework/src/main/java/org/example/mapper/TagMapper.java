package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.Tag;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-15 13:49:50
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}

