package org.example.exception;

import lombok.Getter;
import org.example.enums.AppHttpCodeEnum;

@Getter
public class SystemException extends RuntimeException {

    private final int code;
    private final String msg;

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMessage());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMessage();
    }

}
