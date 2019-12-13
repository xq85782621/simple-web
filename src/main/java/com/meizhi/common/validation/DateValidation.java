package com.meizhi.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 自定义日期格式校验
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface DateValidation {

    String message() default "日期格式错误";

    String format() default "yyyy-MM-dd hh:mm:ss";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

