package app.dao;

import app.domain.Dog;

import java.util.Collection;

public interface DogDao {
    Collection<Dog> getAllDogs();

    Dog getDog(String id);

    Dog createDog(Dog dog);

    Dog updateDog(Dog dog);

    boolean deleteDog(String id);
}
