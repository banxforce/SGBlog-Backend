package org.example.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfoVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    private String status;//账号状态（0正常 1停用）;后台需要

    private String phonenumber;//手机号; 未进行加密处理

    private String sex;

    private String email;


}
