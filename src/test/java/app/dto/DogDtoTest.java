package app.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.util.Set;

import static io.qala.datagen.RandomShortApi.alphanumeric;
import static io.qala.datagen.RandomShortApi.positiveDouble;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void errs_ifTimeOfBirthIsInFuture() {
        OffsetDateTime date = OffsetDateTime.now().plusSeconds(1);
        assertHasOneError(VALIDATOR.validate(dog().setTimeOfBirth(date)), "Time of birth should be in the past.");
    }

    @Test
    public void doesNotErr_ifTimeOfBirthIsInPast() {
        OffsetDateTime timeOfBirth = OffsetDateTime.now().minusSeconds(1);
        assertHasNoError(VALIDATOR.validate(dog().setTimeOfBirth(timeOfBirth)));
    }

    @Test
    public void doesNotErr_ifTimeOfBirthIsNull() {
        assertHasNoError(VALIDATOR.validate(dog().setTimeOfBirth(null)));
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
        assertTrue(violations.isEmpty());
    }

    private static void assertHasOneError(Set<ConstraintViolation<DogDto>> violations, String error) {
        assertEquals(1, violations.size());
        assertEquals(error, violations.iterator().next().getMessage());
    }
}