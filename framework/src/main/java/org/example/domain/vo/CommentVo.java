package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {

    private Long id;

    private Long articleId;//文章id

    private Long rootId;//根评论id

    private String content;//评论内容

    private Long toCommentUserId;//所回复的目标评论的userid

    private String toCommentUserName;//所回复的目标评论的nickname

    private Long toCommentId;//回复目标评论id

    private Long createBy;

    private Date createTime;

    private String username;//当前评论的nickname

    private String avatar; //用户头像

    private List<CommentVo> children;//子评论

}
