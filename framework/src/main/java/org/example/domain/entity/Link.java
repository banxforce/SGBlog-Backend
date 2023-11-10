package org.example.domain.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 友链(Link)表实体类
 *
 * @author makejava
 * @since 2023-09-25 12:48:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sg_link")
public class Link {

    @TableId
    private Long id;

    private String name;

    private String logo;

    private String description;

    private String address;//网站地址

    private String status;//审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    private Integer delFlag;//删除标志（0代表未删除，1代表已删除）
    

}

