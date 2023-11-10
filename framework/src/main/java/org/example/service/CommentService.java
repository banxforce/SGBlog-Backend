package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-10-03 21:02:31
 */
public interface CommentService extends IService<Comment> {

    ResponseResult<Object> commentList(String contentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult<Object> addComment(Comment comment);
}

