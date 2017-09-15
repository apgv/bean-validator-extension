package codes.foobar.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

class PasswordValidatorTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void require_payload_is_set() {
        class Foo {

            @Password
            private String password;

            private Foo(String password) {
                this.password = password;
            }
        }

        Foo foo = new Foo("passwd");

        assertThatThrownBy(() -> validator.validate(foo))
                .isInstanceOf(ValidationException.class)
                .hasCauseExactlyInstanceOf(IllegalArgumentException.class)
                .withFailMessage("Require payload of type %s NoLogging.class");
    }

    @Test
    void valid() {
        Bar bar = new Bar("passwd");

        assertThat(validator.validate(bar)).isEmpty();
    }

    @Test
    void valid_with_many_spaces_characters() {
        Bar bar = new Bar(" p  w ");

        assertThat(validator.validate(bar)).isEmpty();
    }

    @Test
    void null_value() {
        Bar bar = new Bar(null);

        Set<ConstraintViolation<Bar>> constraintViolations = validator.validate(bar);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be between 6 and 12 but was: null");
    }

    @Test
    void too_short() {
        Bar bar = new Bar("foo");

        Set<ConstraintViolation<Bar>> constraintViolations = validator.validate(bar);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be between 6 and 12 but was: 3");
    }

    @Test
    void only_spaces() {
        Bar bar = new Bar("          ");

        Set<ConstraintViolation<Bar>> constraintViolations = validator.validate(bar);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be between 6 and 12 but was: 0");
    }

    @Test
    void too_long() {
        Bar bar = new Bar("13_chars_pass");

        Set<ConstraintViolation<Bar>> constraintViolations = validator.validate(bar);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be between 6 and 12 but was: 13");
    }

    @Test
    void to_short_with_one_char_and_whitespace() {
        Bar bar = new Bar("  2  ");

        Set<ConstraintViolation<Bar>> constraintViolations = validator.validate(bar);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be between 6 and 12 but was: 5");
    }

    private class Bar {

        @Password(min = 6, max = 12, payload = NoLogging.class)
        private String password;

        private Bar(String password) {
            this.password = password;
        }
    }

}