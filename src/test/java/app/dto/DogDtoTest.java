package app.dto;

import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

import static io.qala.datagen.RandomShortApi.alphanumeric;
import static io.qala.datagen.RandomShortApi.positiveDouble;
import static org.assertj.core.api.Assertions.assertThat;

public class DogDtoTest {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void errs_ifNameSizeIsMoreThan100() {
        assertHasOneError(VALIDATOR.validate(dog().setName(alphanumeric(101))), "Name size should be between 1 and 100.");
    }

    @Test
    public void errs_ifNameIsEmpty() {
        assertHasOneError(VALIDATOR.validate(dog().setName("")), "Name size should be between 1 and 100.");
    }

    @Test
    public void errs_ifNameIsNull() {
        assertHasOneError(VALIDATOR.validate(dog().setName(null)), "Name should be specified.");
    }

    @Test
    public void doesNotErr_ifNameSizeIsInRange() {
        assertHasNoError(VALIDATOR.validate(dog().setName(alphanumeric(1))));
        assertHasNoError(VALIDATOR.validate(dog().setName(alphanumeric(1, 100))));
        assertHasNoError(VALIDATOR.validate(dog().setName(alphanumeric(100))));
    }

    @Test
    public void errs_ifBirthDateIsInFuture() {
        Date date = new Date(ZonedDateTime.now().plusSeconds(1).toInstant().toEpochMilli());
        assertHasOneError(VALIDATOR.validate(dog().setBirthDate(date)), "Birth date should be in the past.");
    }

    @Test
    public void doesNotErr_ifBirthDateIsInPast() {
        Date date = new Date(ZonedDateTime.now().minusSeconds(1).toInstant().toEpochMilli());
        assertHasNoError(VALIDATOR.validate(dog().setBirthDate(date)));
    }

    @Test
    public void doesNotErr_ifBirthDateIsNull() {
        assertHasNoError(VALIDATOR.validate(dog().setBirthDate(null)));
    }

    @Test
    public void errs_ifHeightIsZero() {
        assertHasOneError(VALIDATOR.validate(dog().setHeight(.0)), "Height should be greater than 0.");
    }

    @Test
    public void errs_ifHeightIsNull() {
        assertHasOneError(VALIDATOR.validate(dog().setHeight(null)), "Height should be specified.");
    }

    @Test
    public void doesNotErr_ifHeightIsPositive() {
        assertHasNoError(VALIDATOR.validate(dog().setHeight(positiveDouble())));
    }

    @Test
    public void errs_ifWeightIsZero() {
        assertHasOneError(VALIDATOR.validate(dog().setWeight(.0)), "Weight should be greater than 0.");
    }

    @Test
    public void errs_ifWeightIsNull() {
        assertHasOneError(VALIDATOR.validate(dog().setWeight(null)), "Weight should be specified.");
    }

    @Test
    public void doesNotErr_ifWeightIsPositive() {
        assertHasNoError(VALIDATOR.validate(dog().setWeight(positiveDouble())));
    }

    private static DogDto dog() {
        return DogDto.random();
    }

    private static void assertHasNoError(Set<ConstraintViolation<DogDto>> violations) {
        assertThat(violations).isEmpty();
    }

    private static void assertHasOneError(Set<ConstraintViolation<DogDto>> violations, String error) {
        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(error);
    }
}