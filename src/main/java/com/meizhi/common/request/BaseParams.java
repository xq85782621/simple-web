package com.meizhi.common.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Setter
@Getter
public class BaseParams {

    @Min(value = 1,message = "页数 pageNo 不能小于1")
    private Integer pageNo = 1;

    @Min(value = 1,message = "每页展示条数 pageSize 不能小于1")
    @Max(value = 100,message = "每页展示条数 pageSize 不能大于100")
    private Integer pageSize = 5;
}
