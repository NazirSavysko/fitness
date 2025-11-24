package fitness.app.project.fitnessapp.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
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
@Size(min = 10, max = 25, message = "{jakarta.validation.constraints.Email.size}")
@Email
public @interface ValidEmail {

    // 1. Обязательные поля для любой валидации
    String message() default "{jakarta.validation.constraints.Email.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // 2. Поля из стандартной @Email, перенаправленные внутрь (через OverridesAttribute)

    @OverridesAttribute(constraint = Email.class, name = "regexp")
    String regexp() default ".*";

    @OverridesAttribute(constraint = Email.class, name = "flags")
    Pattern.Flag[] flags() default {};
}