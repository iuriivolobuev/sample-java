package app.dto;

import javax.validation.constraints.Size;
import java.util.Date;

public class DogDto {
    private Long id;
    @Size(min = 1, max = 100, message = "Name size should be between 1 and 100.")
    private String name;
    private Date date;
    private Double height;
    private Double weight;

    public Long getId() {
        return id;
    }

    public DogDto setId(Long id) {
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

    public Date getDate() {
        return date;
    }

    public DogDto setDate(Date date) {
        this.date = date;
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
