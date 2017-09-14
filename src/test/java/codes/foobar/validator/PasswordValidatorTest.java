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
    void valid() {
        Foo foo = new Foo("passwd");

        assertThat(validator.validate(foo)).isEmpty();
    }

    @Test
    void valid_with_many_spaces_characters() {
        Foo foo = new Foo(" p  w ");

        assertThat(validator.validate(foo)).isEmpty();
    }

    @Test
    void null_value() {
        Foo foo = new Foo(null);

        Set<ConstraintViolation<Foo>> constraintViolations = validator.validate(foo);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be between 6 and 12 but was: null");
    }

    @Test
    void too_short() {
        Foo foo = new Foo("foo");

        Set<ConstraintViolation<Foo>> constraintViolations = validator.validate(foo);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be between 6 and 12 but was: 3");
    }

    @Test
    void only_spaces() {
        Foo foo = new Foo("          ");

        Set<ConstraintViolation<Foo>> constraintViolations = validator.validate(foo);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be between 6 and 12 but was: 0");
    }

    @Test
    void too_long() {
        Foo foo = new Foo("13_chars_pass");

        Set<ConstraintViolation<Foo>> constraintViolations = validator.validate(foo);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be between 6 and 12 but was: 13");
    }

    @Test
    void to_short_with_one_char_and_whitespace() {
        Foo foo = new Foo("  2  ");

        Set<ConstraintViolation<Foo>> constraintViolations = validator.validate(foo);

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.iterator().next().getMessage())
                .isEqualTo("Password length must be between 6 and 12 but was: 5");
    }

    private class Foo {

        @Password(min = 6, max = 12)
        private String password;

        private Foo(String password) {
            this.password = password;
        }
    }

}