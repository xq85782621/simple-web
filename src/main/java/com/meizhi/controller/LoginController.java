package com.meizhi.controller;

import com.meizhi.common.code.CommonCode;
import com.meizhi.common.exception.CustomException;
import com.meizhi.common.response.ResponseResult;
import com.meizhi.common.util.JwtUtil;
import com.meizhi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用来测试使用jwt+拦截器做的权限验证的controller
 */
@RestController
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 登陆成功签发token
     *
     * @param phone  手机号
     * @param password 密码
     * @return
     */
    @RequestMapping("/login")
    public ResponseResult login(String phone, String password) {
        User user ;
        if ("18656831100".equals(phone) && "xq85782621".equals(password)) {
            user = new User();
            user.setId(1);
            user.setPassword("xq85782621");
            user.setPhone("18656831100");
            user.setUsername("许强");
            user.setEmail("85782621@qq.com");
            String jwt = jwtUtil.createJWT(user);
            return new ResponseResult(jwt);
        } else {
            throw new CustomException(CommonCode.LOGIN_FAIL);
        }
    }



    @RequestMapping(value = "/getUserInfo",name = "user-info")
    public ResponseResult getUserInfo( ){
      return new ResponseResult();
    }

    @RequestMapping(value = "/userAdd",name = "user-add")
    public ResponseResult userAdd( ){
        return new ResponseResult();
    }

    @RequestMapping(value = "/userDelete",name = "user-delete")
    public ResponseResult userDelete( ){
        return new ResponseResult();
    }

}
