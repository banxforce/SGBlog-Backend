package org.example.controller;

import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Comment;
import org.example.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @SystemLog(businessName = "获取文章评论")
    @GetMapping("/commentList")
    public ResponseResult<Object> commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_CONTENT,articleId,pageNum,pageSize);
    }

    @SystemLog(businessName = "获取友链评论")
    @GetMapping("/linkCommentList")
    public ResponseResult<Object> linkCommentList(Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_CONTENT,null,pageNum,pageSize);
    }

    @SystemLog(businessName = "添加评论")
    @PostMapping
    public ResponseResult<Object> addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

}
