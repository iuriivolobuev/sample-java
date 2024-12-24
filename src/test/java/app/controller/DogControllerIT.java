package app.controller;

import app.dto.DogDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Date;

import static io.qala.datagen.RandomShortApi.alphanumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class DogControllerIT {
    @Test
    public void createsDog() {
        DogDto toCreate = DogDto.random();
        long dogId = createDog(toCreate);

        DogDto loaded = getDog(dogId);
        assertDogsEqual(loaded, toCreate);
    }

    @Test
    public void errsIfDogNameIsNotValid_whenCreatingDog() {
        DogDto notValid = DogDto.random().setName(alphanumeric(101));
        createDogWithValidationError(notValid);
    }

    @Test
    public void updatesDog() {
        long dogId = createDog(DogDto.random());
        DogDto toUpdate = DogDto.random();
        updateDog(dogId, toUpdate);

        DogDto loaded = getDog(dogId);
        assertDogsEqual(loaded, toUpdate);
    }

    @Test
    public void errsIfDogDoesNotExist_whenUpdatingDog() {
        updateDogWithNotFoundError(-1, DogDto.random());
    }

    @Test
    public void deletesDog() {
        DogDto expected = new DogDto().setName("Bobby").setBirthDate(new Date()).setHeight(0.35).setWeight(7.5);
        long dogId = createDog(expected);

        DogDto actual = getDog(dogId);
        assertThat(actual).isNotNull();

        deleteDog(dogId);
        RestAssured.given().pathParam("id", dogId).get("/dog/{id}").then().statusCode(404);
    }

    private static DogDto getDog(long dogId) {
        return RestAssured.given().pathParam("id", dogId).get("/dog/{id}").as(DogDto.class);
    }

    private static long createDog(DogDto dog) {
        Response response = RestAssured.given().contentType("application/json").body(dog).when().post("/dog");
        assertEquals(response.getStatusCode(), HttpStatus.OK.value());
        return response.as(DogDto.class).getId();
    }

    private static void createDogWithValidationError(DogDto dog) {
        Response response = RestAssured.given().contentType("application/json").body(dog).when().post("/dog");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertThat(response.as(String.class)).contains("Name size should be between 1 and 100.");
    }

    private static void updateDog(long dogId, DogDto dog) {
        RestAssured.given().pathParam("id", dogId).contentType("application/json").body(dog).when().put("/dog/{id}");
    }

    private static void updateDogWithNotFoundError(long dogId, DogDto dog) {
        Response response = RestAssured.given().pathParam("id", dogId).contentType("application/json").body(dog).when()
                .put("/dog/{id}");
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    private static void deleteDog(long dogId) {
        RestAssured.given().pathParam("id", dogId).when().delete("/dog/{id}");
    }

    private static void assertDogsEqual(DogDto actual, DogDto expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getBirthDate()).isEqualTo(expected.getBirthDate());
        assertThat(actual.getHeight()).isEqualTo(expected.getHeight());
        assertThat(actual.getWeight()).isEqualTo(expected.getWeight());
    }
}