package com.example.ExploRun.service;

import com.example.ExploRun.dto.UserDTO;
import com.example.ExploRun.request.CreateUserRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {

  UserDTO findByUsername(String username);
  UserDTO findByEmail(String email);
  UserDTO findById(UUID id);
  UserDTO createUser(CreateUserRequest createUserRequest);
}
