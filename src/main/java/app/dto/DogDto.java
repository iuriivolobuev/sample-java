package app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static io.qala.datagen.RandomShortApi.*;

public class DogDto {
    private String id;
    @NotNull(message = "Name should be specified.")
    @Size(min = 1, max = 100, message = "Name size should be between 1 and 100.")
    private String name;
    @Past(message = "Time of birth should be in the past.")
    private OffsetDateTime timeOfBirth;
    @NotNull(message = "Height should be specified.")
    @DecimalMin(value = "0", inclusive = false, message = "Height should be greater than 0.")
    private Double height;
    @NotNull(message = "Weight should be specified.")
    @DecimalMin(value = "0", inclusive = false, message = "Weight should be greater than 0.")
    private Double weight;

    public static DogDto random() {
        DogDto dog = new DogDto();
        dog.name = alphanumeric(1, 100);
        Instant randomInstant = Instant.ofEpochMilli(Long(ZonedDateTime.now().withYear(2000).toInstant().toEpochMilli(), System.currentTimeMillis()));
        dog.timeOfBirth = nullOr(OffsetDateTime.ofInstant(randomInstant, ZoneId.systemDefault()));
        dog.height = positiveDouble();
        dog.weight = positiveDouble();
        return dog;
    }

    public String getId() {
        return id;
    }

    public DogDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DogDto setName(String name) {
        this.name = name;
        return this;
    }

    public OffsetDateTime getTimeOfBirth() {
        return timeOfBirth;
    }

    public DogDto setTimeOfBirth(OffsetDateTime timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
        return this;
    }

    public Double getHeight() {
        return height;
    }

    public DogDto setHeight(Double height) {
        this.height = height;
        return this;
    }

    public Double getWeight() {
        return weight;
    }

    public DogDto setWeight(Double weight) {
        this.weight = weight;
        return this;
    }
}
