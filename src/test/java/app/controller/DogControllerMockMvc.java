package app.controller;

import app.dto.DogDto;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;

class DogControllerMockMvc {
    DogControllerMockMvc(MockMvc mvc) {
        RestAssuredMockMvc.mockMvc(mvc);
        RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder().setContentType(ContentType.JSON).build();
    }

    DogDto getDog(long id) {
        MockMvcResponse response = given().get("/dog/{id}", id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(DogDto.class);
    }

    void getDogWithError(long id, HttpStatus status, String error) {
        MockMvcResponse response = given().get("/dog/{id}", id);
        assertThat(response.getStatusCode()).isEqualTo(status.value());
        assertThat(response.as(String.class)).contains(error);
    }

    List<DogDto> getAllDogs() {
        MockMvcResponse response = given().get("/dog");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        return List.of(response.as(DogDto[].class));
    }

    DogDto createDog(DogDto dog) {
        MockMvcResponse response = given().body(dog).post("/dog");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(DogDto.class);
    }

    void createDogWithError(DogDto dog, HttpStatus status, String error) {
        MockMvcResponse response = given().contentType(ContentType.JSON).body(dog).when().post("/dog");
        assertThat(response.getStatusCode()).isEqualTo(status.value());
        assertThat(response.as(String.class)).contains(error);
    }

    void updateDog(long id, DogDto dog) {
        given().contentType(ContentType.JSON).body(dog).when().put("/dog/{id}", id);
    }

    void updateDogWithError(long id, DogDto dog, HttpStatus status, String error) {
        MockMvcResponse response = given().contentType(ContentType.JSON).body(dog).when().put("/dog/{id}", id);
        assertThat(response.getStatusCode()).isEqualTo(status.value());
        assertThat(response.as(String.class)).contains(error);
    }

    void deleteDog(long id) {
        given().when().delete("/dog/{id}", id);
    }
}
