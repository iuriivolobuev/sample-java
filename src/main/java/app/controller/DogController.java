package app.controller;

import app.dto.DogDto;
import app.service.DogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class DogController {
    private static final AtomicLong DOG_ID_GENERATOR = new AtomicLong(-1L);
    private static final Map<Long, DogDto> DOGS = new ConcurrentHashMap<>();

    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @GetMapping("/dog/{id}")
    public ResponseEntity<?> getDog(@PathVariable long id) {
        DogDto dog = DOGS.get(id);
        if (dog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dog);
    }

    @PostMapping("/dog")
    public DogDto createDog(@RequestBody @Valid DogDto dog) {
        dog.setId(DOG_ID_GENERATOR.incrementAndGet());
        DOGS.put(dog.getId(), dog);
        return dog;
    }

    @PutMapping("/dog/{id}")
    public DogDto updateDog(@PathVariable long id, @RequestBody DogDto dog) {
        dog.setId(id);
        return DOGS.put(id, dog);
    }

    @DeleteMapping("/dog/{id}")
    public void deleteDog(@PathVariable long id) {
        DOGS.remove(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> processValidationError(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
