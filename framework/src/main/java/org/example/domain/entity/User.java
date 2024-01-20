package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户表(User)表实体类
 *
 * @author makejava
 * @since 2023-09-25 13:43:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
@Schema(description = "用户实体类")
public class User {

    @TableId
    private Long id;//主键

    @Schema(description = "用户名")
    private String userName;//用户名

    private String nickName;//昵称

    private String password;//密码

    private String type;//用户类型：0代表普通用户，1代表管理员

    private String status;//账号状态（0正常 1停用）

    private String email;//邮箱

    private String phonenumber;//手机号

    private String sex;//用户性别（0男，1女，2未知）

    private String avatar;//头像

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;//创建人的用户id

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;//创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;//更新人

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;//更新时间

    private Integer delFlag;//删除标志（0代表未删除，1代表已删除）
    

}

