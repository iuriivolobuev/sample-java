package app.dto;

import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static io.qala.datagen.RandomShortApi.alphanumeric;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DogDtoTest {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void doesNotErr_ifDogNameIsNull() {
        DogDto dog = new DogDto().setName(null);
        assertTrue(VALIDATOR.validate(dog).isEmpty());
    }

    @Test
    public void errsIfDogNameIsEmpty() {
        DogDto dog = new DogDto().setName("");
        Set<ConstraintViolation<DogDto>> constraintViolations = VALIDATOR.validate(dog);
        assertEquals(constraintViolations.size(), 1);
        assertEquals("Name size should be between 1 and 100.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void errsIfDogNameSizeIsMoreThan100() {
        DogDto dog = new DogDto().setName(alphanumeric(101));
        Set<ConstraintViolation<DogDto>> constraintViolations = VALIDATOR.validate(dog);
        assertEquals(constraintViolations.size(), 1);
        assertEquals("Name size should be between 1 and 100.", constraintViolations.iterator().next().getMessage());
    }
}