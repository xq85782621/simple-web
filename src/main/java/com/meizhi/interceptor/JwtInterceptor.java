package com.meizhi.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.meizhi.common.code.CommonCode;
import com.meizhi.common.exception.CustomException;
import com.meizhi.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Date;

/**
 * 校验token拦截器
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        try {

            // 从请求头中取出 token
            String token = request.getHeader("token");

            // 不存在token则直接抛出异常
            if (StrUtil.isBlank(token)) {
                throw new CustomException(CommonCode.UN_AUTHENTICATED);
            }

            // 解析 token
            Claims claims = jwtUtil.parseJWT(token);

            System.err.println("token签发时间:" + claims.getIssuedAt().toLocaleString());
            System.err.println("token失效时间:" + claims.getExpiration().toLocaleString());

            // 从中取出里面的权限数据
            String apis = (String) claims.get("apis");
            System.err.println("用户拥有的权限是:" + apis);

            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 验证未通过,说明该用户没有这个接口的访问权限
            if (!checkAuthority(apis, handlerMethod)) {
                throw new CustomException(CommonCode.UN_AUTHORISE);
            }

        // 会自动抛出超时异常
        } catch (ExpiredJwtException e) {
            System.err.println("==========================");
        }
        // 说明权限验证通过,放行
        return true;

    }


    public boolean checkAuthority(String apis, HandlerMethod handlerMethod) {
        //获取接口上的 requestMapping 注解的 name
        RequestMapping annotation = handlerMethod.getMethodAnnotation(RequestMapping.class);
        String name = annotation.name();
        System.err.println("用户要访问的接口名是:" + name);
        // 如果没有name说明不需要权限
        if (StrUtil.isNullOrUndefined(name)) {
            return true;
        }
        if (apis.contains(name)) {
            return true;
        }
        return false;

    }


}
