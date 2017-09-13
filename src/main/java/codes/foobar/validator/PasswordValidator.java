package codes.foobar.validator;

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
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("{codes.foobar.validator.Password.message}")
                    .addConstraintViolation();
        }

        return isValid;
    }

    private boolean isNotBlank(String value) {
        return value != null && value.trim().length() > 0;
    }
}
