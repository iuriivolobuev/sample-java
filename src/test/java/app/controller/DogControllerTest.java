package app.controller;

import app.dto.DogDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static org.junit.Assert.assertEquals;

@MockMvcTest
public class DogControllerTest extends AbstractJUnit4SpringContextTests {
    @Autowired
    private DogEndpoints dogs;

    @Test
    public void createsDog() {
        DogDto toCreate = new DogDto().setName("Bobby");
        DogDto result = dogs.createDog(toCreate);
        assertEquals(result.getName(), toCreate.getName());
    }
}