package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.Link;


/**
 * 友链(Link)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-25 12:48:54
 */
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

}

