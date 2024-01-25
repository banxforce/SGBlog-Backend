package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddLinkDto;
import org.example.domain.dto.PageSelectDto;
import org.example.domain.dto.UpdateLinkDto;
import org.example.domain.entity.Link;

import java.io.Serializable;
import java.util.List;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-09-25 12:48:54
 */
public interface LinkService extends IService<Link> {

    ResponseResult<Object> getAllLink();

    ResponseResult<Object> pageList(PageSelectDto pageSelectDto);

    ResponseResult<Object> addLink(AddLinkDto addLinkDto);

    ResponseResult<Object> getLinkById(Serializable id);

    ResponseResult<Object> updateLink(UpdateLinkDto updateLinkDto);

    ResponseResult<Object> deleteByIds(List<? extends Serializable> ids);
}

