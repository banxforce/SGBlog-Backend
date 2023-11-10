package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.Comment;


/**
 * 评论表(Comment)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-03 21:02:31
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}

