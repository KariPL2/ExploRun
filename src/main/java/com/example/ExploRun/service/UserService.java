package com.example.ExploRun.service;

import com.example.ExploRun.dto.UserDTO;
import com.example.ExploRun.entity.User;
import com.example.ExploRun.request.CreateUserRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {

  User findByUsername(String username);
  User findByEmail(String email);
  User findById(UUID id);
  User createUser(CreateUserRequest createUserRequest);
  List<User> findAllUsers();
}
