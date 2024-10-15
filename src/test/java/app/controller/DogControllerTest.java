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
    private DogEndpoints dogs;

    @Test
    public void createsDog() {
        DogDto toCreate = new DogDto().setName("Bobby");
        DogDto result = dogs.createDog(toCreate);
        assertEquals(result.getName(), toCreate.getName());
    }
}