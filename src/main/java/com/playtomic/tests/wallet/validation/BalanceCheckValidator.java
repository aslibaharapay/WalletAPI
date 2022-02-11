package com.playtomic.tests.wallet.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class BalanceCheckValidator implements ConstraintValidator<BalanceCheck, BigDecimal> {

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return value.compareTo(BigDecimal.ZERO) != -1;
    }
}
