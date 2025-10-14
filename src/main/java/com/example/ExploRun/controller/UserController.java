package com.example.ExploRun.controller;

import com.example.ExploRun.dto.UserDTO;
import com.example.ExploRun.entity.User;
import com.example.ExploRun.mapper.UserMapper;
import com.example.ExploRun.request.CreateUserRequest;
import com.example.ExploRun.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;
  private final UserMapper userMapper;

  public UserController(UserService userService, UserMapper userMapper) {
    this.userService = userService;
    this.userMapper = userMapper;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('MANAGE_USERS')")
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    List<User> users = userService.findAllUsers();
    List<UserDTO> userDTOs = users.stream()
        .map(userMapper::toUserDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(userDTOs);
  }
}
