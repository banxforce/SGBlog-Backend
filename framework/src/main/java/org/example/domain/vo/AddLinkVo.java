package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddLinkVo {

    private Long id;

    private String name;

    private String logo;

    private String description;

    private String address;//网站地址

    private String status;//审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)

}
