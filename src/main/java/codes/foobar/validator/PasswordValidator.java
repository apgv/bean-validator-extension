package codes.foobar.validator;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import static java.lang.String.format;
import static java.util.Arrays.stream;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final int MINIMUM_PASSWORD_LENGTH = 4;
    private Class<? extends Payload>[] payload;
    private int min;
    private int max;

    @Override
    public void initialize(Password constraintAnnotation) {
        payload = constraintAnnotation.payload();
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        validateParameters();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean isValid = isNotEmpty(value) &&
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

    private void validateParameters() {
        if (stream(payload).noneMatch(aClass -> aClass.equals(NoLogging.class))) {
            throw new IllegalArgumentException(format("Parameter payload is required to contain %s", NoLogging.class));
        }

        if (min < MINIMUM_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(format("Parameter min must be at least %s", MINIMUM_PASSWORD_LENGTH));
        }

        if (max < min) {
            throw new IllegalArgumentException("Parameter max must be greather or equal to min");
        }
    }

    private boolean isNotEmpty(String value) {
        return !value.trim().isEmpty();
    }

    private String actual(String value) {
        if (isNotEmpty(value)) {
            return String.valueOf(value.length());
        } else {
            return "0";
        }
    }

}
