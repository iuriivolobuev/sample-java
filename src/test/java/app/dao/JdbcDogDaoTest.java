package app.dao;

import app.controller.MockMvcTest;
import app.domain.Dog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static app.TestUtils.assertDatesEqual;
import static io.qala.datagen.RandomShortApi.unicode;
import static io.qala.datagen.RandomValue.length;
import static io.qala.datagen.StringModifier.Impls.suffix;
import static org.junit.Assert.assertEquals;

@MockMvcTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcDogDaoTest {
    @Autowired
    private DogDao dao;

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