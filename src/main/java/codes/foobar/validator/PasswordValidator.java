package codes.foobar.validator;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private int min;
    private int max;

    @Override
    public void initialize(Password constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = isNotBlank(value) &&
                value.length() >= min &&
                value.length() <= max;

        if (!isValid) {
            HibernateConstraintValidatorContext hibernateContext =
                    context.unwrap(HibernateConstraintValidatorContext.class);

            hibernateContext.disableDefaultConstraintViolation();
            hibernateContext
                    .addMessageParameter("actual", actual(value))
                    .buildConstraintViolationWithTemplate("{codes.foobar.validator.Password.message}")
                    .addConstraintViolation();
        }

        return isValid;
    }

    private boolean isNotBlank(String value) {
        return value != null && value.trim().length() > 0;
    }

    private String actual(String value) {
        if (isNotBlank(value)) {
            return String.valueOf(value.length());
        } else {
            return value == null ? "null" : "0";
        }
    }

}
