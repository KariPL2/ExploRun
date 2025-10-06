package com.example.ExploRun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenMetadata {

  private long expirationTimestamp;
  private long issuedAtTimestamp;
  private String userAgent;
  private String ipAddress;
}
