package com.meizhi.common.exception;


import com.meizhi.common.code.ResultCode;

/**
 *    自定义异常
 *
 */
public class CustomException extends RuntimeException {

    private ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
