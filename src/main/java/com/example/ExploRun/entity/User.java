package com.example.ExploRun.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "binary(16)", name = "id")
  private UUID id;

  @NotEmpty
  @Column(name = "username")
  private String username;

  @NotEmpty
  @Column(name = "password")
  private String password;

  @Email
  @NotEmpty
  @Column(unique = true, name = "email")
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private Role role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Route> routes;

  @Override
  public String toString() {
    return "id " + id +
        "username: " + username +
        "email: " + email
        + "routes: " + routes;
  }
}
