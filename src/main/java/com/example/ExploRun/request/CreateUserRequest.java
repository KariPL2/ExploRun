package com.example.ExploRun.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest (
  @NotEmpty(message = "Please provide a username!")
  @Pattern(regexp = "^[A-Za-z0-9@.!#$%&'*+/=?^_`{|}~\\-]+$")
  String username,

  @NotEmpty(message = "Please provide a valid email address!")
  @Email
  String email,

  @NotEmpty(message = "Please provide a password!")
  @Size(min = 6, max = 30, message = "Your password must be between 6 and 30 characters")
  String password
){}
