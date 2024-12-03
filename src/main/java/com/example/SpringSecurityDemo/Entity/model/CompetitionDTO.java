package com.example.SpringSecurityDemo.Entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CompetitionDTO {

    @NotBlank
    @Size(max = 100)
    private String name; // Competition name

    @NotNull
    @JsonFormat(pattern = "yy/MM/dd HH:mm:ss")
    private LocalDateTime departureTime;

    private int pigeonCount; // Number of pigeons (must be positive)

    private int pigeonTotal;

    private int percentage;

    private double latitude;

    private double longitude;

    private Double distance;

    // Constructors
    public CompetitionDTO() {}

    public CompetitionDTO(String name, LocalDateTime departureTime, int pigeonCount, int percentage) {
        this.name = name;
        this.departureTime = departureTime;
        this.pigeonCount = pigeonCount;
        this.percentage = percentage;
    }
}
