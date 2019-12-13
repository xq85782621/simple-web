package com.meizhi.common.validation;


import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrRangeValidator implements ConstraintValidator<StrRangeValidation, String> {
    private StrRangeValidation strRangeValidation;

    @Override
    public void initialize(StrRangeValidation strRangeValidation) {
        this.strRangeValidation = strRangeValidation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // 如果不传类型,说明不根据类型做筛选条件
        if(StrUtil.isBlank(value)){
            return true;
        }
        String[] Type = strRangeValidation.ranges().split(",");

        boolean flag = false;
        for (String s : Type) {
            boolean b = s.trim().equalsIgnoreCase(value);
            if (b){
                flag = true;
                break;
            }
        }

        return flag;
    }


}
