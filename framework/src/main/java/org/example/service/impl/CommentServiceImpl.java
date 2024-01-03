package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Comment;
import org.example.domain.entity.User;
import org.example.domain.vo.CommentVo;
import org.example.domain.vo.PageVo;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.example.mapper.CommentMapper;
import org.example.mapper.UserMapper;
import org.example.service.CommentService;
import org.example.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-10-03 21:02:31
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private UserMapper userMapper;

    @Override
    public ResponseResult<Object> commentList(String contentType, Long articleId, Integer pageNum, Integer pageSize) {
        // 分页查询对应条件的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
                // 只有文章评论有articleId
        queryWrapper.eq(SystemConstants.ARTICLE_CONTENT.equals(contentType),Comment::getArticleId,articleId)
                // 根评论rootId为-1
                .eq(Comment::getRootId, SystemConstants.COMMENT_ROOT_ID)
                // 对应type的评论
                .eq(Comment::getType,contentType);
        // 分页查询
        Page<Comment> commentPage = this.page(Page.of(pageNum, pageSize), queryWrapper);
        // vo优化,填充字段
        List<CommentVo> commentVos = toCommentVoList(commentPage.getRecords());

        // 查询所有根评论对应的子评论集合，并且赋值给对应的属性
        commentVos.forEach(commentVo -> commentVo.setChildren(getChildren(commentVo.getId())));

        // 封装返回
        return ResponseResult.okResult(new PageVo(commentVos,commentPage.getTotal()));
    }

    @Override
    public ResponseResult<Object> addComment(Comment comment) {
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        this.save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id  根评论的id
     * @return 子评论集合
     */
    private List<CommentVo> getChildren(Long id){
        // 按照rootId查找,按时间升序排列
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id)
                .orderByAsc(Comment::getCreateTime);
        List<Comment> comments = this.list(queryWrapper);
        // 为属性赋值并封装成vo
        return toCommentVoList(comments);
    }

    /**
     * 为传入的List<Comment>设置username,toCommentUsername
     * @return List<CommentVo>
     */
    private List<CommentVo> toCommentVoList(List<Comment> comments) {
        // 通过createBy得到评论者的nickName和avatar（头像）,通过toCommentUserId得到被评论者的nickName
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(comments, CommentVo.class);
        commentVos.stream()
                .peek(commentVo -> {
                    User user = userMapper.selectById(commentVo.getCreateBy());
                    // 设置昵称
                    commentVo.setUsername(user.getNickName());
                    // 设置头像
                    commentVo.setAvatar(user.getAvatar());
                    // 回复了其他评论
                    if (commentVo.getToCommentUserId() != -1) {
                        String toCommentUserName = userMapper.selectById(commentVo.getToCommentUserId()).getNickName();
                        commentVo.setToCommentUserName(toCommentUserName);
                    }
                })
                //触发最终计算
                .toList();
        return commentVos;
    }

}

