package org.example.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 标签(Tag)表实体类
 *
 * @author makejava
 * @since 2023-10-15 13:49:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sg_tag")
public class Tag {

    @TableId
    private Long id;

    private String name;//标签名

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Integer delFlag;//删除标志（0代表未删除，1代表已删除）

    private String remark;//备注
    

}

