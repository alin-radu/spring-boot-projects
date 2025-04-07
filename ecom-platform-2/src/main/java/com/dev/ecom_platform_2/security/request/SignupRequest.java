package com.dev.ecom_platform_2.security.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignupRequest {
    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Size(max = 50, message = "Email must not exceed 50 characters.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    private Set<String> role;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters.")
    private String password;
}
