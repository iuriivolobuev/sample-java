package sample.webapp.service;

import sample.webapp.domain.Dog;

import java.util.Collection;

public interface DogService {
    Collection<Dog> getAllDogs();

    Dog getDog(String id);

    Dog createDog(Dog dog);

    Dog updateDog(Dog dog);

    boolean deleteDog(String id);
}
