package com.piatek.rafal.account_service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AdultValidator.class)
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Adult {
    String message() default "client must be adult";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
