package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemGetUserByIdVo {

    private List<Long> roleIds;
    private List<GetAllRoleVo> roles;
    private UserInfoVo user;
}
