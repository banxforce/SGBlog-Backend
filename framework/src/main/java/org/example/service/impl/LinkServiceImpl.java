package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddLinkDto;
import org.example.domain.dto.PageSelectDto;
import org.example.domain.dto.UpdateLinkDto;
import org.example.domain.entity.Link;
import org.example.domain.vo.AddLinkVo;
import org.example.domain.vo.LinkVo;
import org.example.domain.vo.PageLinkVo;
import org.example.domain.vo.PageVo;
import org.example.mapper.LinkMapper;
import org.example.service.LinkService;
import org.example.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-09-25 12:48:54
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Resource
    private LinkMapper linkMapper;

    @Override
    public ResponseResult<Object> getAllLink() {
        //查询所有通过审核的链接
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_PASS);
        List<Link> links = linkMapper.selectList(queryWrapper);
        //vo优化
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult<Object> pageList(PageSelectDto pageSelectDto) {
        // 需要分页查询分类列表。
        Page<Link> page = Page.of(pageSelectDto.getPageNum(), pageSelectDto.getPageSize());
        // 能根据分类名称进行模糊查询。
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StringUtils.hasText(pageSelectDto.getName()), Link::getName, pageSelectDto.getName())
                // 能根据状态进行查询。
                .eq(StringUtils.hasText(pageSelectDto.getStatus()), Link::getStatus, pageSelectDto.getStatus());
        Page<Link> result = this.page(page, queryWrapper);
        // 转换成vo
        List<PageLinkVo> voList = BeanCopyUtils.copyBeanList(result.getRecords(), PageLinkVo.class);
        // 封装返回
        return new ResponseResult<>().ok(new PageVo(voList, result.getTotal()));
    }

    @Override
    public ResponseResult<Object> addLink(AddLinkDto addLinkDto) {
        // Bean转换
        Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        // 保存
        this.save(link);
        // 返回
        return new ResponseResult<>();
    }

    @Override
    public ResponseResult<Object> getLinkById(Serializable id) {
        // 根据id查询
        Link link = this.getById(id);
        // 转换成VO
        AddLinkVo addLinkVo = BeanCopyUtils.copyBean(link, AddLinkVo.class);
        // 返回
        return new ResponseResult<>().ok(addLinkVo);
    }

    @Override
    public ResponseResult<Object> updateLink(UpdateLinkDto updateLinkDto) {
        // Bean转换
        Link link = BeanCopyUtils.copyBean(updateLinkDto, Link.class);
        // 保存
        this.updateById(link);
        // 返回
        return new ResponseResult<>();
    }

    @Override
    public ResponseResult<Object> deleteByIds(List<? extends Serializable> ids) {
        // 根据Id进行逻辑删除
        ids.forEach(this::removeById);
        // 返回
        return new ResponseResult<>();
    }

}

