package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddTagDto;
import org.example.domain.dto.PutTagDto;
import org.example.domain.dto.TagListDto;
import org.example.domain.entity.Tag;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-10-15 13:49:50
 */
public interface TagService extends IService<Tag> {

    ResponseResult<Object> pageListTag(long pageNum, long pageSize, TagListDto tagListDto);

    ResponseResult<Object> addTag(AddTagDto addTagDto);

    ResponseResult<Object> deleteTagById(Long id);

    ResponseResult<Object> getTagById(Long id);

    ResponseResult<Object> updateTag(PutTagDto putTagDto);

    ResponseResult<Object> listAllTag();
}

