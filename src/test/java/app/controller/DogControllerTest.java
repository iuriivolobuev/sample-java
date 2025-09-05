package app.controller;

import app.dto.DogDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static app.TestUtils.assertDatesEqual;
import static io.qala.datagen.RandomShortApi.alphanumeric;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@MockMvcTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DogControllerTest {
    @Autowired
    private DogControllerMockMvc sut;

    @Test
    public void createsDog() {
        DogDto newDog = DogDto.random();
        String id = sut.createDog(newDog).getId();

        DogDto loaded = sut.getDog(id);
        assertDogsEqual(loaded, newDog);
    }

    @Test
    public void errs_ifDogIsNotValid_whenCreatingDog() {
        DogDto notValid = DogDto.random().setName(alphanumeric(101));
        sut.createDogWithError(notValid, HttpStatus.BAD_REQUEST, "Name size should be between 1 and 100.");
    }

    @Test
    public void updatesDog() {
        String id = sut.createDog(DogDto.random()).getId();
        DogDto toUpdate = DogDto.random();
        sut.updateDog(id, toUpdate);

        DogDto loaded = sut.getDog(id);
        assertDogsEqual(loaded, toUpdate);
    }

    @Test
    public void errs_ifDogIsNotValid_whenUpdatingDog() {
        String id = sut.createDog(DogDto.random()).getId();
        DogDto notValid = DogDto.random().setName(alphanumeric(101));
        sut.updateDogWithError(id, notValid, HttpStatus.BAD_REQUEST, "Name size should be between 1 and 100.");
    }

    @Test
    public void errs_ifDogDoesNotExist_whenUpdatingDog() {
        sut.updateDogWithError("-1", DogDto.random(), HttpStatus.NOT_FOUND, "Couldn't find object [Dog] with id=[-1].");
    }

    @Test
    public void deletesDog() {
        String id = sut.createDog(DogDto.random()).getId();

        DogDto actual = sut.getDog(id);
        assertNotNull(actual);

        sut.deleteDog(id);
        sut.getDogWithError(id, HttpStatus.NOT_FOUND, "Couldn't find object [Dog] with id=[%s].".formatted(id));
    }

    @Test
    public void getsAllDogs() {
        String id1 = sut.createDog(DogDto.random()).getId();
        String id2 = sut.createDog(DogDto.random()).getId();
        List<String> ids = sut.getAllDogs().stream().map(DogDto::getId).toList();
        assertThat(ids, hasItems(id1, id2));
    }

    private static void assertDogsEqual(DogDto actual, DogDto expected) {
        assertEquals(expected.getName(), actual.getName());
        assertDatesEqual(expected.getTimeOfBirth(), actual.getTimeOfBirth());
        assertEquals(expected.getHeight(), actual.getHeight());
        assertEquals(expected.getWeight(), actual.getWeight());
    }
}