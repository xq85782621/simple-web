package com.meizhi.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrRangeValidator.class)
public @interface StrRangeValidation {

    String message() default "类型错误";

    String ranges();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
