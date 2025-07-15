package app.domain;

import java.time.OffsetDateTime;

public class Dog {
    private String id;
    private String name;
    private OffsetDateTime timeOfBirth;
    private Double height;
    private Double weight;

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
