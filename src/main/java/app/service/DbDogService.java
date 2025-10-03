package app.service;

import app.dao.DogDao;
import app.domain.Dog;

import java.util.Collection;

public class DbDogService implements DogService {
    private final DogDao dogDao;

    public DbDogService(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    @Override
    public Collection<Dog> getAllDogs() {
        return dogDao.getAllDogs();
    }

    @Override
    public Dog getDog(String id) {
        return dogDao.getDog(id);
    }

    @Override
    public Dog createDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    @Override
    public Dog updateDog(Dog dog) {
        return dogDao.updateDog(dog);
    }

    @Override
    public boolean deleteDog(String id) {
        return dogDao.deleteDog(id);
    }
}
