package com.example.ExploRun.service;

import com.example.ExploRun.dto.RefreshTokenMetadata;

import java.util.Optional;

public interface JwtTokenService {

  void blacklistAccessToken(String accessTokenJti, long expirationMs);

  boolean isAccessTokenBlacklisted(String accessTokenJti);

  void storeSession(String username, String rtJti, RefreshTokenMetadata metadata);

  void revokeSession(String username, String rtJti);

  void revokeAllSessions(String username);

  Optional<RefreshTokenMetadata> getSessionMetadata(String username, String rtJti);

  long getRefreshTokenExpirationMs();
}

