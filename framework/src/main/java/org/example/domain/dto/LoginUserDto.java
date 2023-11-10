package org.example.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户登录Dto")
public class LoginUserDto {

    @Schema(description = "用户名")
    private String userName;//用户名
    @Schema(description = "密码")
    private String password;//密码

}
