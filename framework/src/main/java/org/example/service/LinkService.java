package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-09-25 12:48:54
 */
public interface LinkService extends IService<Link> {

    ResponseResult<Object> getAllLink();
}

