package com.example.ExploRun.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Builder
public class CreateUserRequest {
    @NotEmpty(message = "Please provide a username!")
    @Pattern(regexp = "^[A-Za-z0-9@.!#$%&'*+/=?^_`{|}~\\-]+$")
    private String username;

    @NotEmpty(message = "Please provide a valid email address!")
    @Email
    private String email;

    @NotEmpty(message = "Please provide a password!")
    @Size(min = 6, max = 30, message = "Your password must be between 6 and 30 characters")
    private String password;

    @Builder.Default
    private boolean isPremium = false;
}
