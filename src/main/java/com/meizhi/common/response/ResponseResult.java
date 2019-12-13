package com.meizhi.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meizhi.common.code.CommonCode;
import com.meizhi.common.code.ResultCode;
import lombok.Data;

/**
 *     全局的数据响应对象,所有接口都返回该对象
 *
 *
 *    {
 *      success ：是否成功
 *      code    ：返回码
 *      message ：返回信息
 *      //返回数据
 *      data：  ：{
 *
 *      }
 *    }
 *
 */
@Data
/**
 * 该注解的存在,会使得当属性值为null的时候不参与序列化
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult {

    private boolean success;//是否成功
    private Integer code;// 返回码
    private String message;//返回信息
    private Object data;// 返回数据


    public ResponseResult( ) {
        this(CommonCode.SUCCESS);
    }

    public ResponseResult(ResultCode code) {
        this.success = code.success();
        this.code = code.code();
        this.message = code.message();
    }

    public ResponseResult(Object data) {
        this(CommonCode.SUCCESS);
        this.data = data;
    }

    public ResponseResult(ResultCode code, Object data) {
        this.success = code.success();
        this.code = code.code();
        this.message = code.message();
        this.data = data;
    }

    public ResponseResult(Integer code,String message,boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public static ResponseResult SUCCESS(){
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public static ResponseResult ERROR(){
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }

    public static ResponseResult FAIL(){
        return new ResponseResult(CommonCode.FAIL);
    }
}
