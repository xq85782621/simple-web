package com.meizhi.config;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableMap;
import com.meizhi.common.code.CommonCode;
import com.meizhi.common.code.ResultCode;
import com.meizhi.common.exception.CustomException;
import com.meizhi.common.response.ResponseResult;
import com.meizhi.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisTemplate redisTemplate;


    //定义map的builder对象，去构建ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();
    //定义map，配置异常类型所对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;

    //定义异常类型所对应的错误代码
    static {
        // token 验证失败
        builder.put(SignatureException.class, CommonCode.TOKEN_FAIL);
    }





//    /**
//     * jwt会根据token过期自动抛出异常
//     */
//    @ExceptionHandler(ExpiredJwtException.class)
//    @ResponseBody
//    public ResponseResult ExpiredJwtException(HttpServletRequest request, HttpServletResponse response , BindException e) throws Exception {
//
//        // 从请求头中取出 token
//        String token = request.getHeader("token");
//        // 解析 token
//        Claims claims = jwtUtil.parseJWT(token);
//        Integer userId = (Integer) claims.get("id");
//
//        // 得到 token 失效时间的毫秒值
//        long  expiration = claims.getExpiration().getTime();
//        // 得到 token 的签发时间毫秒值
//        long issuedAt = claims.getIssuedAt().getTime();
//
//        /**
//         *   token 刷新机制 , 避免并发问题
//         *   1.假如token已经超过1个小时失效了,这个时候需要刷新token,返回新token,但是当前页面同时发出了N个请求
//         */
//        // 如果自签发时间超过7天,抛出异常,从新登录
//        if(System.currentTimeMillis() - issuedAt > 7 * 24 * 3600000 ){
//            throw new CustomException(CommonCode.TOKEN_PAST_DUE);
//        }
//
//        // 说明没有超过7天,但是超过了1个小时  1h < time < 7day
//        if( System.currentTimeMillis() - issuedAt > 30000 ){
//            System.err.println("进入了这里..");
//            String newToken = "";
//            // 1.先去redis中查找有没有token
//            String oldToken = (String) redisTemplate.opsForValue().get("userId:" + userId);
//            // 2.如果没有oldToken ,说明该请求是当前页面的并发请求中第一个访问到这里的
//            if(StrUtil.isBlank(oldToken)){
//                // 生成新的token
//                newToken = jwtUtil.getNewToken(token);
//                // 存入到redis中,并设置为30s过期,
//                redisTemplate.opsForValue().set("userId:"+userId, token,30, TimeUnit.SECONDS);
//                response.setHeader("token", newToken);
//            }
//        }
//
//
//        ResponseResult result = new ResponseResult(CommonCode.TOKEN_NEED_REFRESH);
//        return result;
//    }

    /**
     * 中捕获参数校验异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseResult NotValidException(HttpServletRequest req, BindException e) throws Exception {
        BindingResult bindingResult = e.getBindingResult();
        //在这里就能获取所有的校验失败的信息
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        List<String> errMsg = new ArrayList<>();
        for (ObjectError allError : allErrors) {
            String defaultMessage = allError.getDefaultMessage();
            String s = JSONUtil.toJsonStr(allError);
            if (s.contains("Failed to convert property")) {
                defaultMessage = "参数类型不匹配";
            }
            errMsg.add(defaultMessage);
        }
        ResponseResult result = new ResponseResult(CommonCode.PARAMS_VERIFY_FAIL, errMsg);
        return result;
    }

    //捕获CustomException此类异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException customException) {
        //记录日志
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

    //捕获Exception此类异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception exception) {

        System.err.println("出现了未知异常:" + exception);
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();//EXCEPTIONS构建成功
        }
        //从EXCEPTIONS中找异常类型所对应的错误代码，如果找到了将错误代码响应给用户，如果找不到给用户响应99999异常
        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
        if (resultCode != null) {
            return new ResponseResult(resultCode);
        } else {
            //所有非自定义,也未提前预知的异常统一返回99999异常
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }


    }

}
