package com.example.ExploRun.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotEmpty
  private String username;

  @NotEmpty
  private String password;

  @Email
  @NotEmpty
  @Column(unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Override
  public String toString() {
    return "id " + id +
        "username: " + username +
        "email: " + email;
  }
}
