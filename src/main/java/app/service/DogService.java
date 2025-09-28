package app.service;

import app.domain.Dog;

import java.util.Collection;

public interface DogService {
    Collection<Dog> getAllDogs();

    Dog getDog(String id);

    Dog createDog(Dog dog);

    Dog updateDog(Dog dog);

    boolean deleteDog(String id);
}
