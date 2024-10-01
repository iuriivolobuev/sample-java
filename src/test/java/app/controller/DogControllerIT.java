package app.controller;

import app.dto.DogDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Date;

import static io.qala.datagen.RandomShortApi.alphanumeric;
import static org.junit.Assert.*;

public class DogControllerIT {
    @Test
    public void createsDog() {
        DogDto expected = new DogDto().setName("Bobby").setDate(new Date()).setHeight(0.35).setWeight(7.5);
        long dogId = createDog(expected);

        DogDto actual = getDog(dogId);
        assertEquals(actual.getName(), expected.getName());
    }

    @Test
    public void errsIfDogNameIsNotValid_whenCreatingDog() {
        DogDto notValid = new DogDto().setName(alphanumeric(101));
        createDogWithValidationError(notValid);
    }

    @Test
    public void updatesDog() {
        DogDto expected = new DogDto().setName("Bobby").setDate(new Date()).setHeight(0.35).setWeight(7.5);
        long dogId = createDog(expected);

        expected.setName("Tom");
        updateDog(dogId, expected);

        DogDto actual = getDog(dogId);
        assertEquals(actual.getName(), expected.getName());
    }

    @Test
    public void deletesDog() {
        DogDto expected = new DogDto().setName("Bobby").setDate(new Date()).setHeight(0.35).setWeight(7.5);
        long dogId = createDog(expected);

        DogDto actual = getDog(dogId);
        assertNotNull(actual);

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
        assertTrue(response.as(String.class).contains("Name size should be between 1 and 100."));
    }

    private static void updateDog(long dogId, DogDto dog) {
        RestAssured.given().pathParam("id", dogId).contentType("application/json").body(dog).when().put("/dog/{id}");
    }

    private static void deleteDog(long dogId) {
        RestAssured.given().pathParam("id", dogId).when().delete("/dog/{id}");
    }
}