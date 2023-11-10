package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.entity.User;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-25 13:43:49
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

