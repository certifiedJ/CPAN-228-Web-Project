package com.CPAN228.Project.model;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;

@Data
public class RegistrationForm {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm Password is required")
    private String confirmPassword;

    @NotBlank(message = "Role is required")
    private String role;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(null, username, passwordEncoder.encode(password), role.toUpperCase());
    }
}