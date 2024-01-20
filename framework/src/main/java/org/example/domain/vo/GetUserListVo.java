package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserListVo {

    private Long id;//主键

    private String userName;//用户名

    private String nickName;//昵称

    private String status;//账号状态（0正常 1停用）

    private String email;//邮箱

    private String phonenumber;//手机号

    private String sex;//用户性别（0男，1女，2未知）

    private String avatar;//头像

    private Date createTime;//创建时间

    private Long updateBy;//更新人

    private Date updateTime;//更新时间


}
