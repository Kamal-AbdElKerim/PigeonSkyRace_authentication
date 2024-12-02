package com.example.SpringSecurityDemo.Entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String userID ;


    @NotBlank(message = "Username is required and cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]*$",
            message = "Username can only contain letters, numbers, and underscores"
    )
    private String username;


    @NotBlank(message = "Email is required")
    @Email(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Invalid email format"
    )
    private String email;


    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "Phone number must be valid and contain 7 to 15 digits (optional + for country code)"
    )
    private String phone;


    @NotBlank(message = "Address is required and cannot be blank")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9.,'\\-\\s#]*$",
            message = "Address can only contain letters, numbers, spaces, and common punctuation (.,'-#)"
    )
    private String address;


    @NotBlank(message = "Password is required and cannot be blank")
    @Size(min = 6, message = "Minimum password length is 6 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;

}
