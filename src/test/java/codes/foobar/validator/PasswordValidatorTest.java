package codes.foobar.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordValidatorTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void valid_password() {
        Foo foo = new Foo("valid_pw");

        assertThat(validator.validate(foo)).isEmpty();
    }

    @Test
    void valid_password_with_many_spaces_characters() {
        Foo foo = new Foo("  p  ");

        assertThat(validator.validate(foo)).isEmpty();
    }

    @Test
    void null_value_password() {
        Foo foo = new Foo(null);

        Set<ConstraintViolation<Foo>> constraintViolations = validator.validate(foo);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be greater than or equal to 4 but was: null");
    }

    @Test
    void too_short_password() {
        Foo foo = new Foo("foo");

        Set<ConstraintViolation<Foo>> constraintViolations = validator.validate(foo);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be greater than or equal to 4 but was: 3");
    }

    @Test
    void only_spaces_password() {
        Foo foo = new Foo("    ");

        Set<ConstraintViolation<Foo>> constraintViolations = validator.validate(foo);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be greater than or equal to 4 but was: 0");
    }

    private class Foo {

        @Password(minLength = 4)
        private String password;

        private Foo(String password) {
            this.password = password;
        }
    }

}