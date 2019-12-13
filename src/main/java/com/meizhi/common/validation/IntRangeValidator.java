package com.meizhi.common.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntRangeValidator implements ConstraintValidator<IntRangeValidation, Integer> {
    private IntRangeValidation intRangeValidation;

    @Override
    public void initialize(IntRangeValidation intRangeValidation) {
        this.intRangeValidation = intRangeValidation;
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {

        if(value == null){
            return true;
        }

        boolean contains = intRangeValidation.range().contains(value.toString());

        return contains;
    }


}
