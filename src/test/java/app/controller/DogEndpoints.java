package app.controller;

import app.dto.DogDto;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

public class DogEndpoints {
    DogEndpoints(MockMvc mvc) {
        RestAssuredMockMvc.mockMvc(mvc);
        RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder().setContentType(ContentType.JSON).build();
    }

    DogDto createDog(DogDto dog) {
        return given().body(dog).post("/dog").andReturn().as(DogDto.class);
    }
}
