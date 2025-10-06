package com.example.ExploRun.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationResponse {
  private String access_token;
  private String token_type;
  private String refresh_token;
  private Long expires_in;
  private String scope;
}
