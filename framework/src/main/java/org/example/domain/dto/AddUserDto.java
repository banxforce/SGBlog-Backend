package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "新增用户Dto")
public class AddUserDto {

    private String userName;//用户名

    private String nickName;//昵称

    private String password;//密码

//    private String type;//用户类型：0代表普通用户，1代表管理员

    private String status;//账号状态（0正常 1停用）

    private String email;//邮箱

    private String phonenumber;//手机号

    private String sex;//用户性别（0男，1女，2未知）

    private List<Long> roleIds;

}
