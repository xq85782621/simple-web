package com.meizhi.common.validation;

import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

public class DateValidator implements ConstraintValidator<DateValidation, String> {
    private DateValidation dateValidation;

    @Override
    public void initialize(DateValidation dateValidation) {
        this.dateValidation = dateValidation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果 value 为空则不进行格式验证，为空验证可以使用 @NotBlank @NotNull @NotEmpty 等注解来进行控制，职责分离
        if (StrUtil.isBlank(value)) {
            return true;
        }
        String format = dateValidation.format();

        if (value.length() != format.length()) {
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            simpleDateFormat.parse(value);
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
