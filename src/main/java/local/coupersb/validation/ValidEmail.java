package local.coupersb.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.GroupSequence;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static local.coupersb.validation.ValidationUtils.EMAIL_VALIDATION_MESSAGE;
import static local.coupersb.validation.ValidationUtils.EMAIL_NULL_MESSAGE;;

@Email(message = EMAIL_VALIDATION_MESSAGE)
@Pattern(regexp = ".+@.+\\..+", message = EMAIL_VALIDATION_MESSAGE)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotNull(message = EMAIL_NULL_MESSAGE)
@GroupSequence({ NotBlank.class, Email.class })
@ReportAsSingleViolation
@Documented
public @interface ValidEmail 
{
	String message() default EMAIL_VALIDATION_MESSAGE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
