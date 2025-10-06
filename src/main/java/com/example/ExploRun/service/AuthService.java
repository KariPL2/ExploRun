package com.example.ExploRun.service;

import com.example.ExploRun.request.LoginRequestDTO;
import com.example.ExploRun.response.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
  AuthenticationResponse login(LoginRequestDTO loginRequest);

  void logout();

  ResponseEntity<?> refreshToken(String refreshToken);

  void logoutSession(String refreshToken);
}
