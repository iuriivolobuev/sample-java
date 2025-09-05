package app.dao;

import app.domain.Dog;
import app.domain.ObjectNotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused"/*one of the alternative implementations*/)
class InMemoryDogDao implements DogDao {
    private static final Map<String, Dog> DOGS = new ConcurrentHashMap<>();

    @Override
    public Collection<Dog> getAllDogs() {
        return DOGS.values();
    }

    @Override
    public Dog getDog(String id) {
        Dog dog = DOGS.get(id);
        if (dog == null) {
            throw new ObjectNotFoundException(Dog.class, id);
        }
        return dog;
    }

    @Override
    public Dog createDog(Dog dog) {
        DOGS.put(dog.getId(), dog);
        return dog;
    }

    @Override
    public Dog updateDog(Dog dog) {
        String id = dog.getId();
        if (!DOGS.containsKey(id)) {
            throw new ObjectNotFoundException(Dog.class, id);
        }
        DOGS.put(id, dog);
        return dog;
    }

    @Override
    public boolean deleteDog(String id) {
        Dog removed = DOGS.remove(id);
        return removed != null;
    }
}
