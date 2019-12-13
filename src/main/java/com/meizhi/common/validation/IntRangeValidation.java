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
@Constraint(validatedBy = IntRangeValidator.class)
public @interface IntRangeValidation {

    String message() default "类型错误";

    String range();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
