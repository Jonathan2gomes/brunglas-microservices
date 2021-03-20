package org.acme.cadastro.rest.constraints;

import javax.validation.ConstraintValidatorContext;

public interface DTO {
    default boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }

}
