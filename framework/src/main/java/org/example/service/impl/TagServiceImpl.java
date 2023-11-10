package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddTagDto;
import org.example.domain.dto.PutTagDto;
import org.example.domain.dto.TagListDto;
import org.example.domain.entity.Tag;
import org.example.domain.vo.ContentTagVo;
import org.example.domain.vo.PageVo;
import org.example.domain.vo.TagVo;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.example.mapper.TagMapper;
import org.example.service.TagService;
import org.example.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-10-15 13:49:50
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Override
    public ResponseResult<Object> pageListTag(long pageNum, long pageSize, TagListDto tagListDto) {
        // 分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                // 按标签名查询
                .eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName())
                // 按备注查询,前端还未实现
                .eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> tagPage = tagMapper.selectPage(Page.of(pageNum, pageSize), queryWrapper);
        // 封装
        List<TagVo> pageVos = BeanCopyUtils.copyBeanList(tagPage.getRecords(), TagVo.class);
        PageVo pageVo = new PageVo(pageVos, tagPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<Object> addTag(AddTagDto addTagDto) {
        // 标签名不能为空
        if(!StringUtils.hasText(addTagDto.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NOT_NULL);
        }
        // 向表中添加数据,注意公共字段
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Object> deleteTagById(Long id) {
        // 已经配置了逻辑删除字段
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Object> getTagById(Long id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult<Object> updateTag(PutTagDto putTagDto) {
        // 封装
        Tag tag = BeanCopyUtils.copyBean(putTagDto, Tag.class);
        // 更新表中的字段
        updateById(tag);
        // 返回
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Object> listAllTag() {
        // 查询表得到Tag集合
        List<Tag> list = list();
        // 封装
        List<ContentTagVo> voList = BeanCopyUtils.copyBeanList(list, ContentTagVo.class);
        // 返回
        return ResponseResult.okResult(voList);
    }

}

