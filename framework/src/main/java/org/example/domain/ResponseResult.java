package org.example.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.enums.AppHttpCodeEnum;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResponseResult<T> implements Serializable {

    private Integer code;
    private T data;
    private String msg;

    public ResponseResult() {
        this.code = AppHttpCodeEnum.SUCCESS.getCode();
        this.msg = AppHttpCodeEnum.SUCCESS.getMessage();
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static  ResponseResult<Object> errorResult(int code, String msg) {
        ResponseResult<Object> result = new ResponseResult<>();
        return result.error(code, msg);
    }

    public static ResponseResult<Object> okResult() {
        return new ResponseResult<>();
    }

    public static ResponseResult<Object> okResult(int code, String msg) {
        ResponseResult<Object> result = new ResponseResult<>();
        return result.ok(code, null, msg);
    }

    public static ResponseResult<Object> okResult(Object data) {
        ResponseResult<Object> result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getMessage());
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static ResponseResult<Object> errorResult(AppHttpCodeEnum enums) {
        return setAppHttpCodeEnum(enums, enums.getMessage());
    }

    public static ResponseResult<Object> errorResult(AppHttpCodeEnum enums, String msg) {
        return setAppHttpCodeEnum(enums, msg);
    }

    public static ResponseResult<Object> setAppHttpCodeEnum(AppHttpCodeEnum enums) {
        return okResult(enums.getCode(), enums.getMessage());
    }

    private static ResponseResult<Object> setAppHttpCodeEnum(AppHttpCodeEnum enums, String msg) {
        return okResult(enums.getCode(), msg);
    }

    public ResponseResult<T> error(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public ResponseResult<T> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public ResponseResult<T> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        return this;
    }

    public ResponseResult<T> ok(T data) {
        this.data = data;
        return this;
    }

}