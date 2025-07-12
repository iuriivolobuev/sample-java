package app.controller;

import app.dto.DogDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.qala.datagen.RandomShortApi.alphanumeric;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class DogControllerIT {
    @Test
    public void createsDog() {
        DogDto newDog = DogDto.random();
        String id = createDog(newDog).getId();

        DogDto loaded = getDog(id);
        assertDogsEqual(loaded, newDog);
    }

    @Test
    public void errs_ifDogIsNotValid_whenCreatingDog() {
        DogDto notValid = DogDto.random().setName(alphanumeric(101));
        createDogWithError(notValid, HttpStatus.BAD_REQUEST, "Name size should be between 1 and 100.");
    }

    @Test
    public void updatesDog() {
        String id = createDog(DogDto.random()).getId();
        DogDto toUpdate = DogDto.random();
        updateDog(id, toUpdate);

        DogDto loaded = getDog(id);
        assertDogsEqual(loaded, toUpdate);
    }

    @Test
    public void errs_ifDogIsNotValid_whenUpdatingDog() {
        String id = createDog(DogDto.random()).getId();
        DogDto notValid = DogDto.random().setName(alphanumeric(101));
        updateDogWithError(id, notValid, HttpStatus.BAD_REQUEST, "Name size should be between 1 and 100.");
    }

    @Test
    public void errs_ifDogDoesNotExist_whenUpdatingDog() {
        updateDogWithError("-1", DogDto.random(), HttpStatus.NOT_FOUND, "Couldn't find object [DogDto] with id=[-1].");
    }

    @Test
    public void deletesDog() {
        String id = createDog(DogDto.random()).getId();

        DogDto actual = getDog(id);
        assertThat(actual).isNotNull();

        deleteDog(id);
        getDogWithError(id, HttpStatus.NOT_FOUND, "Couldn't find object [DogDto] with id=[%s].".formatted(id));
    }

    @Test
    public void getsAllDogs() {
        String id1 = createDog(DogDto.random()).getId();
        String id2 = createDog(DogDto.random()).getId();
        List<String> ids = getAllDogs().stream().map(DogDto::getId).toList();
        assertThat(ids).contains(id1, id2);
    }

    private static DogDto getDog(String id) {
        Response response = given().get("/dog/{id}", id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(DogDto.class);
    }

    private static void getDogWithError(String id, HttpStatus status, String error) {
        Response response = given().get("/dog/{id}", id);
        assertThat(response.getStatusCode()).isEqualTo(status.value());
        assertThat(response.as(String.class)).contains(error);
    }

    private static List<DogDto> getAllDogs() {
        Response response = given().get("/dog");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        return List.of(response.as(DogDto[].class));
    }

    private static DogDto createDog(DogDto dog) {
        Response response = given().contentType(ContentType.JSON).body(dog).when().post("/dog");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(DogDto.class);
    }

    private static void createDogWithError(DogDto dog, HttpStatus status, String error) {
        Response response = given().contentType(ContentType.JSON).body(dog).when().post("/dog");
        assertThat(response.getStatusCode()).isEqualTo(status.value());
        assertThat(response.as(String.class)).contains(error);
    }

    private static void updateDog(String id, DogDto dog) {
        given().contentType(ContentType.JSON).body(dog).when().put("/dog/{id}", id);
    }

    private static void updateDogWithError(String id, DogDto dog, HttpStatus status, String error) {
        Response response = given().contentType(ContentType.JSON).body(dog).when().put("/dog/{id}", id);
        assertThat(response.getStatusCode()).isEqualTo(status.value());
        assertThat(response.as(String.class)).contains(error);
    }

    private static void deleteDog(String id) {
        given().when().delete("/dog/{id}", id);
    }

    private static void assertDogsEqual(DogDto actual, DogDto expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getBirthDate()).isEqualTo(expected.getBirthDate());
        assertThat(actual.getHeight()).isEqualTo(expected.getHeight());
        assertThat(actual.getWeight()).isEqualTo(expected.getWeight());
    }
}