package codes.foobar.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private int minLength;

    @Override
    public void initialize(Password constraintAnnotation) {
        minLength = constraintAnnotation.minLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = value != null &&
                value.trim().length() > 0 &&
                value.length() >= minLength;

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("{codes.foobar.validator.Password.message}")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
