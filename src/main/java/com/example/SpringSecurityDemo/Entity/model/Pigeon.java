package com.example.SpringSecurityDemo.Entity.model;

import com.example.SpringSecurityDemo.Entity.User.Breeder;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "pigeons")
@Getter
@Setter
@ToString
@NoArgsConstructor // Lombok generates a no-args constructor
@AllArgsConstructor // Lombok generates a constructor with all fields
@Builder // Optional: Lombok generates a builder pattern for easy object creation
public class Pigeon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Auto-generated ID for the primary key

    @NotNull(message = "Ring number cannot be null")
    @Min(value = 1, message = "Ring number must be a positive integer")
    private Long ringNumber; // Unique ring number for each pigeon

    @NotBlank(message = "Gender cannot be blank")
    private String gender;
    @PositiveOrZero(message = "Age must be zero or a positive integer")
    private int age;
    @Size(max = 50, message = "Color cannot exceed 50 characters")
    private String color;

    @ManyToOne
    @JoinColumn(name = "breeder_id") // Foreign key to Breeder entity
    private Breeder breeder;

    @ManyToMany
    @JoinTable(
            name = "competition_pigeon",
            joinColumns = @JoinColumn(name = "pigeon_id"),
            inverseJoinColumns = @JoinColumn(name = "competition_id")
    )
    private Set<CompetitionPigeon> competitions;

    // The constructors are generated by Lombok
    // No need for additional constructor code since Lombok handles it
}
