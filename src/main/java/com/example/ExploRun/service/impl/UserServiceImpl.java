package com.example.ExploRun.service.impl;

import com.example.ExploRun.dto.UserDTO;
import com.example.ExploRun.entity.Role;
import com.example.ExploRun.entity.User;
import com.example.ExploRun.mapper.UserMapper;
import com.example.ExploRun.repository.UserRepository;
import com.example.ExploRun.request.CreateUserRequest;
import com.example.ExploRun.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  @Override
  public User findByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow( () ->
            new NoSuchElementException("User not found!"));
  }

  @Override
  public User findByEmail(String email) {
    return (userRepository
        .findByEmail(email)
        .orElseThrow( () ->
            new NoSuchElementException("User not found!")));
  }

  @Override
  public User findById(UUID id) {
    return (userRepository
        .findById(id)
        .orElseThrow( () ->
            new NoSuchElementException("User not found!")));
  }

  @Override
  public User createUser(CreateUserRequest createUserRequest) {
    User user = new User();
    user.setUsername(createUserRequest.getUsername());
    user.setEmail(createUserRequest.getEmail());
    user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
    user.setRole(createUserRequest.isPremium() ? Role.PREMIUM_RUNNER : Role.RUNNER);
    return userRepository.save(user);
  }

  @Override
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }
}
