package fitness.app.project.fitnessapp.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {})
@DecimalMin(value = "0.01", inclusive = true, message = "Weight must be positive")
@DecimalMax(value = "9999.99", inclusive = true, message = "Weight must not exceed 9999.99")
@Digits(integer = 4, fraction = 2, message = "Weight must have up to 4 integer digits and 2 decimal places")
public @interface ValidWeight {

    String message() default "Invalid weight";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
