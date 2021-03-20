package org.acme.cadastro.rest.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.CLASS)
@Constraint(validatedBy = { ValidDTOValidator.class })
@Documented
public @interface  ValidDTO {

    String message() default "{com.acme.cadastro.dto.ValidDTO.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
