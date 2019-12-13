package com.meizhi.controller;

import com.meizhi.common.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class IndexController {


    @GetMapping("/")
    public ResponseResult index(){
        return new ResponseResult();
    }


}
