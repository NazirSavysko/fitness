package fitness.app.project.fitnessapp.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Size(min = 10, max = 30, message = "{jakarta.validation.constraints.Surname.size}")
@Pattern(regexp = "^[A-Z][a-zA-Z0-9]*$",
        message = "{jakarta.validation.constraints.Surname.pattern}")
public @interface ValidSurname {

    String message() default "Invalid surname format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}