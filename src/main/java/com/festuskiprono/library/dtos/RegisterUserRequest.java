package com.festuskiprono.library.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message="Name is required")
    @Size(max=255,message="Name should be less than 255 characters")
    private String name;
    @NotBlank(message = "Email must be valid")
    private String email;
    @NotBlank(message="Password is required")
    @Size(min=8,max=25, message ="Password should be between 6 and 25 characters")
    private String password;
}