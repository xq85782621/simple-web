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
import java.util.concurrent.TimeUnit;

/**
 * 校验token拦截器
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate redisTemplate;

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
            Claims claims = e.getClaims();
            long issuedAt = claims.getIssuedAt().getTime();
            long expireTime = claims.getExpiration().getTime();
            Integer userId = (Integer) claims.get("id");

            // 如果过期了一个星期了，就不必刷新token了，直接抛出异常，让用户从新登陆
            if (System.currentTimeMillis() - issuedAt > 7 * 24 * 3600000) {
                throw new CustomException(CommonCode.TOKEN_PAST_DUE);
            }

            // 说明只是当前token过期了
            System.err.println("执行刷新token逻辑。，。");
            String newToken = "";
            // 1.先去redis中查找有没有token
            String oldToken = (String) redisTemplate.opsForValue().get("userId:" + userId);
            // 2.如果没有oldToken ,说明该请求是当前页面的并发请求中第一个访问到这里的
            if (StrUtil.isBlank(oldToken)) {
                // 生成新的token
                newToken = jwtUtil.getNewToken(claims);
                // 存入到redis中,并设置为30s过期,
                redisTemplate.opsForValue().set("userId:" + userId, request.getHeader("token"), 30, TimeUnit.SECONDS);
                response.setHeader("token", newToken);

            }


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
