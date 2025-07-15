package app.controller;

import app.dao.DogDao;
import app.domain.ObjectNotFoundException;
import app.dto.DogDto;
import app.service.DogService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
public class DogController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DogService dogService;
    private final DogDao dogDao;

    public DogController(DogService dogService, DogDao dogDao) {
        this.dogService = dogService;
        this.dogDao = dogDao;
    }

    @GetMapping("/dog")
    public Collection<DogDto> getAllDogs() {
        logger.info("Getting all the dogs.");
        return DogDto.fromDomain(dogDao.getAllDogs());
    }

    @GetMapping("/dog/{id}")
    public DogDto getDog(@PathVariable String id) {
        logger.info("Getting a dog: id=[{}].", id);
        return DogDto.fromDomain(dogDao.getDog(id));
    }

    @PostMapping("/dog")
    public DogDto createDog(@RequestBody @Valid DogDto dog) {
        logger.info("Creating a dog: [{}].", dog);
        dog.setId(UUID.randomUUID().toString());
        return DogDto.fromDomain(dogDao.createDog(dog.toDomain()));
    }

    @PutMapping("/dog/{id}")
    public DogDto updateDog(@PathVariable String id, @RequestBody @Valid DogDto dog) {
        logger.info("Updating a dog: id=[{}].", id);
        dog.setId(id);
        dogDao.updateDog(dog.toDomain());
        return dog;
    }

    @DeleteMapping("/dog/{id}")
    public void deleteDog(@PathVariable String id) {
        logger.info("Deleting a dog: id=[{}].", id);
        dogDao.deleteDog(id);
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
