package app.domain;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static io.qala.datagen.RandomShortApi.*;

public class Dog {
    private String id;
    private String name;
    private OffsetDateTime timeOfBirth;
    private Double height;
    private Double weight;

    public static Dog random() {
        return new Dog()
                .setName(alphanumeric(1, 100))
                .setTimeOfBirth(randomOffsetDateTime())
                .setHeight(positiveDouble())
                .setWeight(positiveDouble());
    }

    private static OffsetDateTime randomOffsetDateTime() {
        Instant randomInstant = Instant.ofEpochMilli(Long(ZonedDateTime.now().withYear(2000).toInstant().toEpochMilli(), System.currentTimeMillis()));
        return nullOr(OffsetDateTime.ofInstant(randomInstant, ZoneId.systemDefault()));
    }

    public String getId() {
        return id;
    }

    public Dog setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Dog setName(String name) {
        this.name = name;
        return this;
    }

    public OffsetDateTime getTimeOfBirth() {
        return timeOfBirth;
    }

    public Dog setTimeOfBirth(OffsetDateTime timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
        return this;
    }

    public Double getHeight() {
        return height;
    }

    public Dog setHeight(Double height) {
        this.height = height;
        return this;
    }

    public Double getWeight() {
        return weight;
    }

    public Dog setWeight(Double weight) {
        this.weight = weight;
        return this;
    }
}
