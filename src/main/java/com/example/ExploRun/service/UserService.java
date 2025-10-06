package com.example.ExploRun.service;

import com.example.ExploRun.dto.UserDTO;
import com.example.ExploRun.entity.User;
import com.example.ExploRun.mapper.UserMapper;
import com.example.ExploRun.request.CreateUserRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface UserService {

  UserDTO findByUsername(String username);
  UserDTO findByEmail(String email);
  UserDTO findById(Long id);
  UserDTO createUser(CreateUserRequest createUserRequest);
}
