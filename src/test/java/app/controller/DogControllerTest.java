package app.controller;

import app.dto.DogDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@MockMvcTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DogControllerTest {
    @Autowired
    private DogControllerMockMvc sut;

    @Test
    public void createsDog() {
        DogDto toCreate = DogDto.random();
        DogDto result = sut.createDog(toCreate);
        assertEquals(result.getName(), toCreate.getName());
    }
}