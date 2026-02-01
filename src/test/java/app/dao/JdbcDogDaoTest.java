package app.dao;

import app.controller.MockMvcTest;
import app.domain.Dog;
import org.h2.jdbc.JdbcSQLTransactionRollbackException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static app.TestUtils.assertDatesEqual;
import static io.qala.datagen.RandomShortApi.unicode;
import static io.qala.datagen.RandomValue.length;
import static io.qala.datagen.StringModifier.Impls.suffix;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@MockMvcTest
@RunWith(SpringJUnit4ClassRunner.class)
@SuppressWarnings("resource")
public class JdbcDogDaoTest {
    @Autowired
    private DogDao dao;
    @Autowired
    private JdbcConnectionHolder connections;

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

    @Test
    public void simulatesDeadlockInDb() {
        Dog dog1 = dao.createDog(Dog.random().setId(UUID.randomUUID()));
        Dog dog2 = dao.createDog(Dog.random().setId(UUID.randomUUID()));

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<?> f1 = executorService.submit(() -> updateDogs(dog1.setName("upd11"), dog2.setName("upd12")));
        Future<?> f2 = executorService.submit(() -> updateDogs(dog2.setName("upd21"), dog1.setName("upd22")));

        ExecutionException exception = assertThrows(ExecutionException.class, () -> {
            f1.get();
            f2.get();
        });

        JdbcSQLTransactionRollbackException rollbackException = findCause(exception, JdbcSQLTransactionRollbackException.class);
        assertThat(rollbackException.getMessage(), startsWith("Deadlock detected. The current transaction was rolled back."));

        executorService.shutdownNow();
    }

    private static void assertDogsEqual(Dog expected, Dog actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertDatesEqual(expected.getTimeOfBirth(), actual.getTimeOfBirth());
        assertEquals(expected.getHeight(), actual.getHeight());
        assertEquals(expected.getWeight(), actual.getWeight());
    }

    private void updateDogs(Dog dog1, Dog dog2) {
        try {
            connections.beginTransaction();
            dao.updateDog(dog1);
            Thread.sleep(10);
            dao.updateDog(dog2);
            connections.commit();
        } catch (Exception e) {
            connections.rollback();
            throw new RuntimeException(e);
        } finally {
            connections.closeCurrentConnection();
        }
    }

    private static <T> T findCause(Throwable t, Class<T> type) {
        while (t != null) {
            if (type.isInstance(t)) {
                //noinspection unchecked
                return (T) t;
            }
            t = t.getCause();
        }
        return null;
    }
}