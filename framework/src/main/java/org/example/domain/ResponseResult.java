package org.example.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.enums.AppHttpCodeEnum;

import java.io.Serializable;

// 这个类是一个通用的响应结果封装类，用于构建统一的接口返回格式。下面我会对类和方法进行解释注释。
@JsonInclude(JsonInclude.Include.NON_NULL) // 序列化时不包含空字段
@Data
public class ResponseResult<T> implements Serializable {
    private Integer code; // 响应状态码
    private T data; // 响应数据
    private String msg; // 响应消息

    // 无参构造方法，默认设置为成功状态
    public ResponseResult() {
        this.code = AppHttpCodeEnum.SUCCESS.getCode();
        this.msg = AppHttpCodeEnum.SUCCESS.getMessage();
    }

    // 构造方法，传入状态码和数据
    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    // 构造方法，传入状态码、消息和数据
    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 构造方法，传入状态码和消息
    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // 返回错误的响应结果
    public static ResponseResult<Object> errorResult(int code, String msg) {
        ResponseResult<Object> result = new ResponseResult<>();
        return result.error(code, msg);
    }

    // 返回成功的响应结果
    public static ResponseResult<Object> okResult() {
        return new ResponseResult<>();
    }

    // 返回指定状态码和消息的成功响应结果
    public static ResponseResult<Object> okResult(int code, String msg) {
        ResponseResult<Object> result = new ResponseResult<>();
        return result.ok(code, null, msg);
    }

    // 返回成功的响应结果，包含数据
    public static ResponseResult<Object> okResult(Object data) {
        ResponseResult<Object> result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getMessage());
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    // 返回指定 AppHttpCodeEnum 的错误响应结果
    public static ResponseResult<Object> errorResult(AppHttpCodeEnum enums) {
        return setAppHttpCodeEnum(enums, enums.getMessage());
    }

    // 返回指定 AppHttpCodeEnum、指定消息的错误响应结果
    public static ResponseResult<Object> errorResult(AppHttpCodeEnum enums, String msg) {
        return setAppHttpCodeEnum(enums, msg);
    }

    // 返回指定 AppHttpCodeEnum 的成功响应结果
    public static ResponseResult<Object> setAppHttpCodeEnum(AppHttpCodeEnum enums) {
        return okResult(enums.getCode(), enums.getMessage());
    }

    // 返回指定 AppHttpCodeEnum、指定消息的成功响应结果
    private static ResponseResult<Object> setAppHttpCodeEnum(AppHttpCodeEnum enums, String msg) {
        return okResult(enums.getCode(), msg);
    }

    // 设置错误状态码和消息，并返回当前对象
    public ResponseResult<T> error(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    // 设置成功状态码和数据，并返回当前对象
    public ResponseResult<T> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    // 设置成功状态码、数据和消息，并返回当前对象
    public ResponseResult<T> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        return this;
    }

    // 设置成功状态码、数据，并返回当前对象
    public ResponseResult<T> ok(T data) {
        this.data = data;
        return this;
    }
}
