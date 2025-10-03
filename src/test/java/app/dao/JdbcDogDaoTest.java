package app.dao;

import app.controller.MockMvcTest;
import app.domain.Dog;
import app.service.DogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static app.TestUtils.assertDatesEqual;
import static io.qala.datagen.RandomShortApi.unicode;
import static io.qala.datagen.RandomValue.length;
import static io.qala.datagen.StringModifier.Impls.suffix;
import static org.junit.Assert.assertEquals;

@MockMvcTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcDogDaoTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcDogDaoTest.class);

    @Autowired
    private DogDao dao;
    @Autowired
    private DogService dogService;

    @Test
    public void name() throws InterruptedException {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        Dog dog1 = dogService.createDog(Dog.random().setId(id1));
        Dog dog2 = dogService.createDog(Dog.random().setId(id2));

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            LOGGER.info("task1");
            dogService.updateDogs(dog1.setName("a"), dog2.setName("b"));
        });
        executorService.submit(() -> {
            LOGGER.info("task2");
            dogService.updateDogs(dog2.setName("c"), dog1.setName("d"));
        });

        executorService.shutdown();//initiates shutdown, doesn't block
        boolean afterAwait = executorService.awaitTermination(100, TimeUnit.SECONDS);//blocks until all tasks are completed or timeout occurs
    }

    @Test
    public void dbCanHoldMaxValues_thatJavaCanHold() {
        Dog original = Dog.random().setId(UUID.randomUUID().toString()).setName(unicode(100))
                .setHeight(Double.MAX_VALUE).setWeight(Double.MAX_VALUE);
        dao.createDog(original);
        Dog fromDb = dao.getDog(original.getId());
        assertDogsEqual(original, fromDb);
    }

    @Test
    public void dbOperationsAreProtectedFromSqlInjections() {
        String sqlInjection = length(20).with(suffix("'\"")).english();
        Dog original = Dog.random().setId(sqlInjection).setName(sqlInjection);
        dao.createDog(original);

        Dog loaded = dao.getDog(sqlInjection);
        assertDogsEqual(original, loaded);

        Dog toUpdate = Dog.random().setId(sqlInjection).setName(sqlInjection);
        dao.updateDog(toUpdate);

        dao.deleteDog(sqlInjection);
    }

    private static void assertDogsEqual(Dog expected, Dog actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertDatesEqual(expected.getTimeOfBirth(), actual.getTimeOfBirth());
        assertEquals(expected.getHeight(), actual.getHeight());
        assertEquals(expected.getWeight(), actual.getWeight());
    }
}