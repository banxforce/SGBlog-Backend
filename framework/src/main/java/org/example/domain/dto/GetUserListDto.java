package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户列表Dto")
public class GetUserListDto {

    private String userName;//用户名
    private String status;//账号状态（0正常 1停用）
    private String phonenumber;//手机号

}
