package app.service;

import app.dao.JdbcConnectionHolder;
import app.domain.Dog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@SuppressWarnings("resource")
public class TransactionalDogService implements DogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DogService.class);

    private final JdbcConnectionHolder connections;
    private final DogService dogService;

    public TransactionalDogService(JdbcConnectionHolder connections, DogService dogService) {
        this.connections = connections;
        this.dogService = dogService;
    }

    @Override
    public Collection<Dog> getAllDogs() {
        Collection<Dog> result;
        try {
            connections.beginTransaction();
            result = dogService.getAllDogs();
            connections.commit();
        } catch (Exception e) {
            connections.rollback();
            throw e;
        } finally {
            connections.closeCurrentConnection();
        }
        return result;
    }

    @Override
    public Dog getDog(String id) {
        Dog result;
        try {
            connections.beginTransaction();
            result = dogService.getDog(id);
            connections.commit();
        } catch (Exception e) {
            connections.rollback();
            throw e;
        } finally {
            connections.closeCurrentConnection();
        }
        return result;
    }

    @Override
    public Dog createDog(Dog dog) {
        Dog result;
        try {
            connections.beginTransaction();
            result = dogService.createDog(dog);
            connections.commit();
        } catch (Exception e) {
            connections.rollback();
            throw e;
        } finally {
            connections.closeCurrentConnection();
        }
        return result;
    }

    @Override
    public Dog updateDog(Dog dog) {
        Dog result;
        try {
            connections.beginTransaction();
            result = dogService.updateDog(dog);
            connections.commit();
        } catch (Exception e) {
            connections.rollback();
            throw e;
        } finally {
            connections.closeCurrentConnection();
        }
        return result;
    }

    @Override
    public Dog updateDogs(Dog dog1, Dog dog2) {
        LOGGER.info("dog1={}, dog2={}", dog1.getId(), dog2.getId());
        Dog result;
        try {
            connections.beginTransaction();
            result = dogService.updateDog(dog1);
            LOGGER.info("{} updated {}", Thread.currentThread().getName(), dog1.getId());
            Thread.sleep(3000L);
            LOGGER.info("{} starts updating {}", Thread.currentThread().getName(), dog2.getId());
            result = dogService.updateDog(dog2);
            connections.commit();
        } catch (Exception e) {
            connections.rollback();
            throw new RuntimeException(e);
        } finally {
            connections.closeCurrentConnection();
        }
        return result;
    }

    @Override
    public boolean deleteDog(String id) {
        boolean result;
        try {
            connections.beginTransaction();
            result = dogService.deleteDog(id);
            connections.commit();
        } catch (Exception e) {
            connections.rollback();
            throw e;
        } finally {
            connections.closeCurrentConnection();
        }
        return result;
    }
}
