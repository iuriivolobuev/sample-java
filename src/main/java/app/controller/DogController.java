package app.controller;

import app.domain.ObjectNotFoundException;
import app.dto.DogDto;
import app.service.DogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class DogController {
    private static final AtomicLong DOG_ID_GENERATOR = new AtomicLong(-1L);
    private static final Map<Long, DogDto> DOGS = new ConcurrentHashMap<>();

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @GetMapping("/dog")
    public Collection<DogDto> getAllDogs() {
        logger.info("Getting all the dogs.");
        return DOGS.values();
    }

    @GetMapping("/dog/{id}")
    public DogDto getDog(@PathVariable long id) {
        logger.info("Getting a dog: id=[{}].", id);
        DogDto dog = DOGS.get(id);
        if (dog == null) {
            throw new ObjectNotFoundException(DogDto.class, id);
        }
        return dog;
    }

    @PostMapping("/dog")
    public DogDto createDog(@RequestBody @Valid DogDto dog) {
        logger.info("Creating a dog: [{}].", dog);
        dog.setId(DOG_ID_GENERATOR.incrementAndGet());
        DOGS.put(dog.getId(), dog);
        return dog;
    }

    @PutMapping("/dog/{id}")
    public DogDto updateDog(@PathVariable long id, @RequestBody DogDto dog) {
        logger.info("Updating a dog: id=[{}].", id);
        dog.setId(id);
        return DOGS.put(id, dog);
    }

    @DeleteMapping("/dog/{id}")
    public void deleteDog(@PathVariable long id) {
        logger.info("Deleting a dog: id=[{}].", id);
        DOGS.remove(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> processValidationError(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> processObjectNotFoundException(ObjectNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
