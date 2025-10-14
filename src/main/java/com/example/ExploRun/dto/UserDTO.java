package com.example.ExploRun.dto;

import com.example.ExploRun.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  private String username;
  private String email;
  private Role role;
  private Set<RouteDTO> routes;
}
