package com.example.ExploRun.controller;

import com.example.ExploRun.dto.UserDTO;
import com.example.ExploRun.mapper.UserMapper;
import com.example.ExploRun.request.CreateUserRequest;
import com.example.ExploRun.request.LoginRequestDTO;
import com.example.ExploRun.request.RefreshTokenRequest;
import com.example.ExploRun.response.AuthenticationResponse;
import com.example.ExploRun.service.AuthService;
import com.example.ExploRun.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final UserService userService;
  private final UserMapper userMapper;

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(
      @Valid @RequestBody LoginRequestDTO loginRequest) {
    AuthenticationResponse authenticationResponse = authService.login(loginRequest);
    return ResponseEntity.ok(authenticationResponse);
  }

  @DeleteMapping("/logout")
  public ResponseEntity<String> logout() {
    authService.logout();
    return ResponseEntity.ok("You have been signed out");
  }

  @DeleteMapping("/logout-session")
  public ResponseEntity<String> logoutSession(@Valid @RequestBody RefreshTokenRequest request) {
    authService.logoutSession(request.getRefreshToken());
    return ResponseEntity.ok("You have been signed out from current session");
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenRequest request) {
    String refreshToken = request.getRefreshToken();
    return authService.refreshToken(refreshToken);
  }

  @PostMapping("/register")
  public ResponseEntity<UserDTO>  register(@Valid @RequestBody CreateUserRequest createUserRequest) {
    UserDTO userDTO = userMapper.toUserDTO(userService.createUser(createUserRequest));
    return ResponseEntity.ok(userDTO);
  }
}
