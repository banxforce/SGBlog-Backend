package org.example.enums;

import lombok.Getter;

@Getter
public enum AppHttpCodeEnum {
    //成功
    SUCCESS(200,"操作成功"),
    //登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATION_AUTH(403,"无操作权限"),
    SYSTEM_ERROR(500,"出现错误"),
    PHONENUMBER_EXIST(502,"手机号已经存在"),
    REQUIRE_USERNAME(504,"必须填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOT_NULL(506,"评论内容不能为空"),
    FILE_TYPE_ERROR(507,"文件只能为JPG,JPEG,PNG格式"),
    USERNAME_NOT_NULL(508,"用户名不能为空"),USERNAME_EXIST(501,"用户名已经存在"),
    PASSWORD_NOT_NULL(509,"密码不能为空"),
    EMAIL_NOT_NULL(510,"邮箱不能为空"),EMAIL_EXIST(503,"邮箱已经存在"),
    NICKNAME_NOT_NULL(511,"昵称不能为空"), NICKNAME_EXIST(512, "昵称已存在"),
    PASSWORD_FORMAT_ERROR(513,"密码只能由6-12位的字母、数字和下划线组成"),
    TAG_NAME_NOT_NULL(514,"标签名不能为空");


    private int code;
    private String message;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code=code;
        this.message=errorMessage;
    }


}
