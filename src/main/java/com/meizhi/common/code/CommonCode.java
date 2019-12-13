package com.meizhi.common.code;

/**
 *      公共的返回码,
 *      返回码code：
 *      成功：10000
 *      失败：10001
 *      未登录：10002
 *      未授权：10003
 *      抛出异常：99999
 */
public enum CommonCode implements ResultCode {

    SUCCESS(true,10000,"操作成功！"),

    //---系统错误返回码-----
    FAIL(false,10001,"操作失败"),
    UN_AUTHENTICATED(false,10002,"您还未登录"),
    UN_AUTHORISE(false,10003,"权限不足"),
    SERVER_ERROR(false,99999,"抱歉，系统繁忙，请稍后重试！"),
    PARAMS_VERIFY_FAIL(false,00000,"入参错误,请检查后重试"),
    SQL_ERROR(false,50000,"SQL执行异常,请联系管理员");

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    CommonCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }



    public boolean success() {
        return success;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}
