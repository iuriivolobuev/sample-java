package app.service;

import app.dao.JdbcConnectionHolder;
import app.domain.Dog;

import java.util.Collection;

@SuppressWarnings("resource")
public class TransactionalDogService implements DogService {
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
