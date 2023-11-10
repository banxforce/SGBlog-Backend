package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Link;
import org.example.domain.vo.LinkVo;
import org.example.mapper.LinkMapper;
import org.example.service.LinkService;
import org.example.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

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

}

